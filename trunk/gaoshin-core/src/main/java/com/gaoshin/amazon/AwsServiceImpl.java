package com.gaoshin.amazon;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.beans.Location;
import com.gaoshin.beans.User;
import com.gaoshin.business.BaseServiceImpl;
import com.gaoshin.dao.LocationDao;
import com.gaoshin.dao.UserDao;
import com.gaoshin.entity.UserEntity;
import com.gaoshin.entity.UserLocationEntity;
import common.util.GeoUtil;
import common.web.BusinessException;
import common.web.ServiceError;

@Service("awsService")
@Transactional
public class AwsServiceImpl extends BaseServiceImpl implements AwsService {
    @Autowired
    private AwsDao awsDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private LocationDao locationDao;

    @Override
    public AwsBrowseNodeList getUnloadedBrowseNodes(int size) {
        List<BrowseNodeEntity> entities = awsDao.findEntityBy(BrowseNodeEntity.class, "loaded", false, 0, 10);
        AwsBrowseNodeList result = new AwsBrowseNodeList();
        for (BrowseNodeEntity entity : entities) {
            result.getList().add(entity.getBean());
        }
        return result;
    }

    @Override
    public AwsBrowseNodeList getUnitemedBrowseNodes(int size) {
        List<BrowseNodeEntity> entities = awsDao.findEntityBy(BrowseNodeEntity.class, "itemed", false, 0, 10);
        AwsBrowseNodeList result = new AwsBrowseNodeList();
        for (BrowseNodeEntity entity : entities) {
            result.getList().add(entity.getBean());
        }
        return result;
    }

    @Override
    public void addNodes(List<AwsBrowseNode> tbd) {
        for (AwsBrowseNode node : tbd) {
            BrowseNodeEntity entity = awsDao.getEntity(BrowseNodeEntity.class, node.getId());
            if (entity == null) {
                entity = new BrowseNodeEntity(node);
                entity.setLoaded(true);
                awsDao.saveEntity(entity);
            } else if (!entity.isLoaded()) {
                entity.copyFrom(node);
                entity.setLoaded(true);
                awsDao.saveEntity(entity);
            }

            List<ItemEntity> topItems = entity.getTopItems();
            if (topItems.size() == 0 && node.getTopSellers().size() > 0) {
                for (AwsItem ai : node.getTopSellers()) {
                    ItemEntity itemEntity = awsDao.getEntity(ItemEntity.class, ai.getAsin());
                    if (itemEntity == null) {
                        itemEntity = new ItemEntity();
                        itemEntity.setAsin(ai.getAsin());
                        itemEntity.setLoaded(false);
                        awsDao.saveEntity(itemEntity);
                    }
                    entity.getTopItems().add(itemEntity);
                }
                awsDao.saveEntity(entity);
            }

            for (AwsBrowseNode child : node.getChildren()) {
                BrowseNodeEntity childEntity = awsDao.getEntity(BrowseNodeEntity.class, child.getId());
                if (childEntity == null) {
                    childEntity = new BrowseNodeEntity(child);
                    awsDao.saveEntity(childEntity);
                }
                if (!entity.hasChild(childEntity.getId())) {
                    entity.getChildren().add(childEntity);
                }
            }

            for (AwsBrowseNode parent : node.getParents()) {
                BrowseNodeEntity parentEntity = awsDao.getEntity(BrowseNodeEntity.class, parent.getId());
                if (parentEntity == null) {
                    parentEntity = new BrowseNodeEntity(parent);
                    awsDao.saveEntity(parentEntity);
                }
                if (!entity.hasParent(parentEntity.getId())) {
                    entity.getParents().add(parentEntity);
                }
            }
            awsDao.saveEntity(entity);
        }
    }

    @Override
    public void setBadNode(String id) {
        BrowseNodeEntity entity = awsDao.getEntity(BrowseNodeEntity.class, id);
        if (entity != null) {
            entity.setBadNode(true);
            entity.setRetry(entity.getRetry() + 1);
            entity.resetLastUpdate();
            entity.setLoaded(entity.getRetry() > 5);
            awsDao.saveEntity(entity);
        }
    }

    @Override
    public void setBadNodes(List<String> idList) {
        for (String id : idList) {
            setBadNode(id);
        }
    }

    @Override
    public void setNodeItemed(String id) {
        BrowseNodeEntity entity = awsDao.getEntity(BrowseNodeEntity.class, id);
        if (entity != null) {
            entity.setItemed(true);
            awsDao.saveEntity(entity);
        }
    }

    @Override
    public void setNodeItemed(List<String> idList) {
        for (String id : idList) {
            setNodeItemed(id);
        }
    }

    @Override
    public void setNodeListItemed(List<AwsBrowseNode> idList) {
        for (AwsBrowseNode id : idList) {
            setNodeItemed(id.getId());
        }
    }

    @Override
    public AwsBrowseNodeList getTopBrowseNodes() {
        String sql = "select id , name from aws_node where loaded=1 and bad_node=0 and id not in (select child_id from aws_node_tree) order by name";
        List<Object[]> list = awsDao.nativeQuerySelect(sql);
        AwsBrowseNodeList result = new AwsBrowseNodeList();
        for (Object[] item : list) {
            AwsBrowseNode bean = new AwsBrowseNode();
            bean.setId(item[0].toString());
            bean.setName(item[1] == null ? "null" : item[1].toString());
            result.getList().add(bean);
        }
        return result;
    }

    @Override
    public AwsBrowseNode getBrowseNode(String nodeId) {
        BrowseNodeEntity entity = awsDao.getEntity(BrowseNodeEntity.class, nodeId);
        if (entity == null) {
            entity = new BrowseNodeEntity();
            entity.setId(nodeId);
            awsDao.saveEntity(entity);
        }
        return getBrowseNode(entity);
    }

    public AwsBrowseNode getBrowseNode(BrowseNodeEntity entity) {
        AwsBrowseNode bean = entity.getBean();
        for (BrowseNodeEntity child : entity.getChildren()) {
            bean.getChildren().add(child.getBean());
        }
        bean.setParents(getParents(entity));

        List<NodeInterestEntity> nieList = awsDao.findEntityBy(NodeInterestEntity.class, "nodeEntity.id", entity.getId());
        for (NodeInterestEntity nie : nieList) {
            NodeInterest interest = nie.getBean(NodeInterest.class);
            interest.setLocation(nie.getUserLocationEntity().getBean(Location.class));
            interest.setUser(nie.getUserEntity().getBean(User.class));
            bean.getInterests().add(interest);
        }

        return bean;
    }

    public List<AwsBrowseNode> getParents(BrowseNodeEntity entity) {
        List<AwsBrowseNode> list = new ArrayList<AwsBrowseNode>();
        for (BrowseNodeEntity parentEntity : entity.getParents()) {
            AwsBrowseNode parent = parentEntity.getBean();
            list.add(parent);
            parent.setParents(getParents(parentEntity));
        }
        return list;
    }

    @Override
    public void addUnloadedNodes(List<AwsBrowseNode> tbd) {
        for (AwsBrowseNode node : tbd) {
            BrowseNodeEntity entity = awsDao.getEntity(BrowseNodeEntity.class, node.getId());
            if (entity == null) {
                entity = new BrowseNodeEntity(node);
                entity.setLoaded(false);
                awsDao.saveEntity(entity);
            }
        }
    }

    @Override
    public void addUnloadedItems(List<AwsItem> tbd) {
        for (AwsItem item : tbd) {
            ItemEntity entity = awsDao.getEntity(ItemEntity.class, item.getAsin());
            if (entity == null) {
                if (item.getTitle() != null && item.getTitle().length() > 200)
                    item.setTitle(item.getTitle().substring(0, 200));
                entity = new ItemEntity(item);
                entity.setLoaded(false);
                awsDao.saveEntity(entity);
            }
        }
    }

    @Override
    public AwsItemList getUnloadedItems(int i) {
        List<ItemEntity> entities = awsDao.findEntityBy(ItemEntity.class, "loaded", false, 0, 10);
        AwsItemList result = new AwsItemList();
        for (ItemEntity entity : entities) {
            result.getList().add(entity.getBean());
        }
        return result;
    }

    @Override
    public void setBadItems(ArrayList<String> nodeIdList) {
        for (String asin : nodeIdList) {
            ItemEntity entity = awsDao.getEntity(ItemEntity.class, asin);
            entity.setLoaded(true);
            awsDao.saveEntity(entity);
        }
    }

    @Override
    public void setItemsLoaded(ArrayList<String> nodeIdList) {
        for (String asin : nodeIdList) {
            ItemEntity entity = awsDao.getEntity(ItemEntity.class, asin);
            entity.setLoaded(true);
            awsDao.saveEntity(entity);
        }
    }

    @Override
    public AwsBrowseNodeList search(String keywords) {
        if (keywords.startsWith("select")) {
            String sql = keywords;
            List<Object[]> list = awsDao.nativeQuerySelect(sql);
            AwsBrowseNodeList result = new AwsBrowseNodeList();
            for (Object[] item : list) {
                AwsBrowseNode bean = getBrowseNode(item[0].toString());
                result.getList().add(bean);
            }
            return result;
        } else {
            String sql = "select id , name from aws_node where match(name) against (? in boolean mode)";
            List<Object[]> list = awsDao.nativeQuerySelect(sql, keywords);
            AwsBrowseNodeList result = new AwsBrowseNodeList();
            for (Object[] item : list) {
                AwsBrowseNode bean = getBrowseNode(item[0].toString());
                result.getList().add(bean);
            }
            return result;
        }
    }

    @Override
    public AwsItem getItem(String asin) {
        ItemEntity entity = awsDao.getEntity(ItemEntity.class, asin);
        if (entity == null) {
            entity = new ItemEntity();
            entity.setAsin(asin);
            awsDao.saveEntity(entity);
        }
        return getItem(entity);
    }

    @Override
    public AwsItem getItem(String asin, float latitude, float longitude, int miles) {
        ItemEntity entity = awsDao.getEntity(ItemEntity.class, asin);
        if (entity == null) {
            entity = new ItemEntity();
            entity.setAsin(asin);
            awsDao.saveEntity(entity);
        }
        return getItem(entity, latitude, longitude, miles);
    }

    public AwsItem getItem(ItemEntity entity, float latitude, float longitude, int miles) {
        if (entity == null)
            throw new BusinessException(ServiceError.NotFound);
        AwsItem bean = entity.getBean();
        List<ItemInterestEntity> entities = awsDao.findEntityBy(ItemInterestEntity.class, "itemEntity.asin", entity.getAsin());
        for (ItemInterestEntity iie : entities) {
            if (iie.getUserLocationEntity() == null || iie.getUserLocationEntity().getLatitude() == null || iie.getUserLocationEntity().getLongitude() == null)
                continue;
            double distance = GeoUtil.distance(latitude, longitude, iie.getUserLocationEntity().getLatitude(), iie.getUserLocationEntity().getLongitude());
            if (distance > miles)
                continue;
            ItemInterest iib = iie.getBean(ItemInterest.class);
            if (iie.getUserEntity() != null)
                iib.setUser(iie.getUserEntity().getBean(User.class));
            bean.getListItemInterest().add(iib);
            iib.setLocation(iie.getUserLocationEntity().getBean(Location.class));
        }
        return bean;
    }

    public AwsItem getItem(ItemEntity entity) {
        if (entity == null)
            throw new BusinessException(ServiceError.NotFound);
        AwsItem bean = entity.getBean();
        List<ItemInterestEntity> entities = awsDao.findEntityBy(ItemInterestEntity.class, "itemEntity.asin", entity.getAsin());
        for (ItemInterestEntity iie : entities) {
            ItemInterest iib = iie.getBean(ItemInterest.class);
            if (iie.getUserEntity() != null)
                iib.setUser(iie.getUserEntity().getBean(User.class));
            bean.getListItemInterest().add(iib);
            iib.setLocation(iie.getUserLocationEntity().getBean(Location.class));
        }
        return bean;
    }

    @Override
    public AwsItem addInterest(User user, ItemInterest interest) {
        UserEntity userEntity = userDao.getUser(user);
        if (userEntity == null)
            throw new BusinessException(ServiceError.Forbidden);
        if (interest.isSell() && (interest.getLocation() == null || interest.getLocation().getId() == null)) {
            throw new BusinessException(ServiceError.InvalidInput, "Location is required");
        }
        if (interest.getItem() == null || interest.getItem().getAsin() == null) {
            throw new BusinessException(ServiceError.InvalidInput, "Item is required");
        }
        ItemEntity itemEntity = awsDao.getEntity(ItemEntity.class, interest.getItem().getAsin());
        if (itemEntity == null) {
            itemEntity = new ItemEntity();
            itemEntity.setAsin(interest.getItem().getAsin());
            awsDao.saveEntity(itemEntity);
        }

        UserLocationEntity locationEntity = null;
        if (interest.getLocation() != null && interest.getLocation().getId() != null) {
            locationEntity = userDao.getEntity(UserLocationEntity.class, interest.getLocation().getId());
        } else {
            List<UserLocationEntity> locationEntities = locationDao.getUserLocations(userEntity.getId());
            for (UserLocationEntity ule : locationEntities) {
                if (ule.isDevice()) {
                    locationEntity = ule;
                    break;
                }
            }
            if (locationEntity == null) {
                locationEntity = new UserLocationEntity();
                locationEntity.setDevice(true);
                locationEntity.setUserEntity(userEntity);
                locationDao.saveEntity(locationEntity);
            }
        }

        if (locationEntity == null) {
            throw new BusinessException(ServiceError.InvalidInput, "Location doesn't exist");
        }
        if (!locationEntity.getUserEntity().getId().equals(userEntity.getId())) {
            throw new BusinessException(ServiceError.Forbidden, "Location specified is not right");
        }

        ItemInterestEntity entity = awsDao.getFirstEntityBy(ItemInterestEntity.class, "userEntity.id", userEntity.getId(), "itemEntity.asin", interest.getItem().getAsin());
        if (entity != null)
            throw new BusinessException(ServiceError.InvalidInput, "item " + interest.getItem().getAsin() + " is already in user " + userEntity.getId() + " interest list");

        entity = new ItemInterestEntity(interest);
        entity.setUserEntity(userEntity);
        entity.setUserLocationEntity(locationEntity);
        entity.setItemEntity(itemEntity);
        awsDao.saveEntity(entity);

        // itemEntity.getListItemInterestEntity().add(entity);
        // awsDao.saveEntity(itemEntity);

        notifyInterest(entity);

        return getItem(itemEntity);
    }

    private void notifyInterest(ItemInterestEntity entity) {
    }

    @Override
    public AwsItem removeInterest(User user, Long interestId) {
        return null;
    }

    @Override
    public AwsBrowseNode addNodeInterest(NodeInterest interest) {
        if (interest.getInfo() == null || interest.getInfo().length() == 0)
            throw new BusinessException(ServiceError.InvalidInput, "Info is required");

        UserEntity userEntity = userDao.getUser(interest.getUser());
        if (userEntity == null)
            throw new BusinessException(ServiceError.Forbidden);
        if (interest.isSell() && (interest.getLocation() == null || interest.getLocation().getId() == null)) {
            throw new BusinessException(ServiceError.InvalidInput, "Location is required");
        }
        if (interest.getNode() == null || interest.getNode().getId() == null) {
            throw new BusinessException(ServiceError.InvalidInput, "Node is required");
        }
        BrowseNodeEntity itemEntity = awsDao.getEntity(BrowseNodeEntity.class, interest.getNode().getId());
        if (itemEntity == null) {
            itemEntity = new BrowseNodeEntity();
            itemEntity.setId(interest.getNode().getId());
            awsDao.saveEntity(itemEntity);
        }

        UserLocationEntity locationEntity = null;
        if (interest.getLocation() != null && interest.getLocation().getId() != null) {
            locationEntity = userDao.getEntity(UserLocationEntity.class, interest.getLocation().getId());
        } else {
            List<UserLocationEntity> locationEntities = locationDao.getUserLocations(userEntity.getId());
            for (UserLocationEntity ule : locationEntities) {
                if (ule.isDevice()) {
                    locationEntity = ule;
                    break;
                }
            }
            if (locationEntity == null) {
                locationEntity = new UserLocationEntity();
                locationEntity.setDevice(true);
                locationEntity.setUserEntity(userEntity);
                locationDao.saveEntity(locationEntity);
            }
        }

        if (locationEntity == null) {
            throw new BusinessException(ServiceError.InvalidInput, "Location doesn't exist");
        }
        if (!locationEntity.getUserEntity().getId().equals(userEntity.getId())) {
            throw new BusinessException(ServiceError.Forbidden, "Location specified is not right");
        }

        NodeInterestEntity entity = awsDao.getFirstEntityBy(NodeInterestEntity.class, "userEntity.id", userEntity.getId(), "nodeEntity.id", interest.getNode().getId());
        if (entity != null)
            throw new BusinessException(ServiceError.InvalidInput, "item " + interest.getNode().getId() + " is already in user " + userEntity.getId() + " interest list");

        entity = new NodeInterestEntity(interest);
        entity.setUserEntity(userEntity);
        entity.setUserLocationEntity(locationEntity);
        entity.setNodeEntity(itemEntity);
        awsDao.saveEntity(entity);

        notifyInterest(entity);

        return getBrowseNode(itemEntity);
    }

    private void notifyInterest(NodeInterestEntity entity) {
    }

    @Override
    public NodeInterestList getNodeInterest(User user, String nodeId) {
        BrowseNodeEntity entity = awsDao.getEntity(BrowseNodeEntity.class, nodeId);
        NodeInterestList list = new NodeInterestList();
        if (entity != null) {
            List<NodeInterestEntity> nieList = awsDao.findEntityBy(NodeInterestEntity.class, "nodeEntity.id", nodeId);
            for (NodeInterestEntity nie : nieList) {
                list.getList().add(nie.getBean(NodeInterest.class));
            }
        }
        return list;
    }
}
