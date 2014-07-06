package com.gaoshin.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.beans.Category;
import com.gaoshin.beans.Dimension;
import com.gaoshin.beans.DimensionValue;
import com.gaoshin.beans.Location;
import com.gaoshin.beans.ObjectBean;
import com.gaoshin.beans.ObjectBeanList;
import com.gaoshin.beans.ObjectCategory;
import com.gaoshin.dao.ObjectDao;
import com.gaoshin.dao.UserDao;
import com.gaoshin.entity.CatDimRelationEntity;
import com.gaoshin.entity.CategoryEntity;
import com.gaoshin.entity.DimensionEntity;
import com.gaoshin.entity.DimensionValueEntity;
import com.gaoshin.entity.ObjectDimensionValueEntity;
import com.gaoshin.entity.ObjectEntity;
import com.gaoshin.entity.UserEntity;
import com.gaoshin.entity.UserLocationEntity;
import common.util.reflection.ReflectionUtil;
import common.web.BusinessException;
import common.web.ServiceError;

@Service("objectService")
@Transactional
public class ObjectServiceImpl extends BaseServiceImpl implements ObjectService {
    public static final String ROOT_CATEGORY_NAME = "ROOCAT";

    @Autowired
    private ObjectDao objectDao;

    @Autowired
    private UserDao userDao;

    private Long rootCategoryId = null;

    @Override
    public Category createCategory(Category category) {
        if ((category.getParent() == null || category.getParent().getId() == null) && !ROOT_CATEGORY_NAME.equals(category.getName())) {
            Category parent = new Category();
            category.setParent(parent);
            parent.setId(rootCategoryId);
        }

        if (objectDao.getFirstEntityBy(CategoryEntity.class, "name", category.getName()) != null) {
            throw new BusinessException(ServiceError.Duplicated);
        }

        CategoryEntity parentEntity = null;
        if (category.getParent() != null && category.getParent().getId() != null) {
            parentEntity = objectDao.getEntity(CategoryEntity.class, category.getParent().getId());
        }
        CategoryEntity entity = new CategoryEntity(category);
        entity.setParent(parentEntity);
        objectDao.saveEntity(entity);

        if (parentEntity != null) {
            parentEntity.getChildren().add(entity);
            objectDao.saveEntity(parentEntity);
        }

        return getCategory(entity.getId());
    }

    @Override
    public Category getCategory(Long id) {
        CategoryEntity entity = objectDao.getEntity(CategoryEntity.class, id);
        return getCategory(entity);
    }

    private Category getCategory(CategoryEntity entity) {
        Category ret = new Category();

        ReflectionUtil.copyPrimeProperties(ret, entity);

        for (CategoryEntity childEntity : entity.getChildren()) {
            Category childBean = new Category();
            ReflectionUtil.copyPrimeProperties(childBean, childEntity);
            ret.getChildren().add(childBean);
        }

        for (CatDimRelationEntity catDimRelEntity : entity.getDimensions()) {
            Dimension dimBean = new Dimension();
            DimensionEntity dimensionEntity = catDimRelEntity.getDimension();
            ReflectionUtil.copyPrimeProperties(dimBean, dimensionEntity);
            ret.getDimensions().add(dimBean);

            for (DimensionValueEntity dve : dimensionEntity.getDimensionValues()) {
                DimensionValue bean = dve.getBean(DimensionValue.class);
                bean.setDimvalue(dve.getId());
                dimBean.getValues().add(bean);
            }
        }

        CategoryEntity parentEntity = entity.getParent();
        if (parentEntity != null) {
            Category parentBean = getCategory(parentEntity);
            ret.setParent(parentBean);
        }

        return ret;
    }

    @Override
    public Category addDimension(Long categoryId, Long dimensionId) {
        CategoryEntity catEntity = objectDao.getEntity(CategoryEntity.class, categoryId);
        if (catEntity == null) {
            throw new BusinessException(ServiceError.NotFound);
        }

        if (catEntity.hasDimension(dimensionId)) {
            throw new BusinessException(ServiceError.Duplicated);
        }

        DimensionEntity dimEntity = objectDao.getEntity(DimensionEntity.class, dimensionId);
        if (dimEntity == null) {
            throw new BusinessException(ServiceError.NotFound);
        }

        CatDimRelationEntity relation = new CatDimRelationEntity();
        relation.setCatId(categoryId);
        relation.setDimId(dimensionId);
        relation.setCategory(catEntity);
        relation.setDimension(dimEntity);
        objectDao.saveEntity(relation);

        catEntity.getDimensions().add(relation);
        objectDao.saveEntity(catEntity);

        dimEntity.getCategories().add(relation);
        objectDao.saveEntity(dimEntity);

        return getCategory(catEntity.getId());
    }

    @Override
    public void removeDimension(Long dimensionId) {
        DimensionEntity entity = objectDao.getEntity(DimensionEntity.class, dimensionId);

        for (CatDimRelationEntity rel : entity.getCategories()) {
            CategoryEntity cat = rel.getCategory();
            CatDimRelationEntity dimension = cat.removeDimension(dimensionId);
            objectDao.removeEntity(dimension);
            objectDao.saveEntity(cat);
        }

        entity.setCategories(null);
        objectDao.removeEntity(entity);
    }

    @Override
    public DimensionValue addDimensionValue(Long dimensionId, DimensionValue dimensionValue) {
        DimensionEntity dimEntity = objectDao.getEntity(DimensionEntity.class, dimensionId);
        DimensionValueEntity entity = new DimensionValueEntity(dimensionValue);
        entity.setDimension(dimEntity);
        objectDao.saveEntity(entity);

        dimEntity.getDimensionValues().add(entity);
        objectDao.saveEntity(dimEntity);

        return dimensionValue;
    }

    @Override
    public void removeDimensionValue(Long dimensionValueId) {
        DimensionValueEntity entity = objectDao.getEntity(DimensionValueEntity.class, dimensionValueId);
        DimensionEntity dim = entity.getDimension();
        dim.removeValue(dimensionValueId);
        objectDao.saveEntity(dim);
        objectDao.removeEntity(entity);
    }

    @Override
    public void removeCategory(Long categoryId) {
        if (categoryId.equals(rootCategoryId)) {
            throw new BusinessException(ServiceError.Forbidden);
        }

        CategoryEntity entity = objectDao.getEntity(CategoryEntity.class, categoryId);
        entity.setDimensions(null);
        objectDao.saveEntity(entity);
        objectDao.removeEntity(entity);
    }

    @Override
    public void init() {
        objectDao.init();

        // create root category if not exist
        CategoryEntity rootCategory = objectDao.getFirstEntityBy(CategoryEntity.class, "name", ROOT_CATEGORY_NAME);
        if (rootCategory == null) {
            rootCategory = new CategoryEntity();
            rootCategory.setName(ROOT_CATEGORY_NAME);
            objectDao.saveEntity(rootCategory);
        }
        rootCategoryId = rootCategory.getId();
    }

    @Override
    public Dimension createDimension(Dimension dimension) {
        verifyBeans(dimension, "create");

        DimensionEntity entity = objectDao.getFirstEntityBy(DimensionEntity.class, "name", dimension.getName());
        if (entity != null)
            throw new BusinessException(ServiceError.Duplicated);

        entity = new DimensionEntity(dimension);
        objectDao.saveEntity(entity);

        for (DimensionValue dv : dimension.getValues()) {
            DimensionValueEntity dve = new DimensionValueEntity(dv);
            dve.setDimension(entity);
            objectDao.saveEntity(dve);
        }

        return getDimension(entity.getId());
    }

    @Override
    public Dimension getDimension(Long dimensionId) {
        DimensionEntity entity = objectDao.getEntity(DimensionEntity.class, dimensionId);
        if (entity == null)
            throw new BusinessException(ServiceError.NotFound);

        Dimension bean = entity.getBean(Dimension.class);
        
        for (DimensionValueEntity valueEntity : entity.getDimensionValues()) {
            bean.getValues().add(valueEntity.getBean(DimensionValue.class));
        }
        
        for (CatDimRelationEntity rel : entity.getCategories()) {
            CategoryEntity catEntity = rel.getCategory();
            bean.getCategories().add(catEntity.getBean(Category.class));
        }

        return bean;
    }

    @Override
    public Dimension getDimensionByName(String name) {
        DimensionEntity entity = objectDao.getFirstEntityBy(DimensionEntity.class, "name", name);
        if (entity == null)
            return null;

        Dimension bean = entity.getBean(Dimension.class);

        for (DimensionValueEntity valueEntity : entity.getDimensionValues()) {
            DimensionValue dimensionValue = valueEntity.getBean(DimensionValue.class);
            dimensionValue.setDimvalue(valueEntity.getId());
            bean.getValues().add(dimensionValue);
        }

        for (CatDimRelationEntity rel : entity.getCategories()) {
            CategoryEntity catEntity = rel.getCategory();
            bean.getCategories().add(catEntity.getBean(Category.class));
        }

        return bean;
    }

    @Override
    public void removeDimension(Long categoryId, Long dimensionId) {
        DimensionEntity entity = objectDao.getEntity(DimensionEntity.class, dimensionId);
        if (entity == null)
            throw new BusinessException(ServiceError.NotFound);

        CategoryEntity catEntity = objectDao.getEntity(CategoryEntity.class, categoryId);
        if (catEntity == null)
            throw new BusinessException(ServiceError.NotFound);

        if (!catEntity.hasDimension(dimensionId)) {
            throw new BusinessException(ServiceError.NotFound);
        }

        CatDimRelationEntity dimension = catEntity.removeDimension(dimensionId);
        objectDao.removeEntity(dimension);
        objectDao.saveEntity(catEntity);
    }

    @Override
    public ObjectBean createObject(ObjectBean bean) {
        if (bean.getCategories() == null || bean.getCategories().size() == 0) {
            throw new BusinessException(ServiceError.InvalidInput, "Object must belong to at least one category.");
        }
        
        if(bean.getLocation() == null || bean.getLocation().getId() == null) {
            throw new BusinessException(ServiceError.InvalidInput, "Must specify a location");
        }
        
        if(bean.getUser() == null || bean.getUser().getId() == null) {
            throw new BusinessException(ServiceError.InvalidInput, "who?");
        }
        
        ObjectEntity entity = new ObjectEntity(bean);
        UserEntity userEntity = userDao.getEntity(UserEntity.class, bean.getUser().getId());
        entity.setUser(userEntity);
        UserLocationEntity locationEntity = objectDao.getEntity(UserLocationEntity.class, bean.getLocation().getId());
        if (!locationEntity.getUserEntity().getId().equals(userEntity.getId())) {
            throw new BusinessException(ServiceError.Forbidden);
        }
        entity.setLocation(locationEntity);
        objectDao.saveEntity(entity);

        return getObjectBean(entity.getId());
    }

    @Override
    public ObjectBean getObjectBean(Long objectId) {
        ObjectEntity entity = objectDao.getEntity(ObjectEntity.class, objectId);
        ObjectBean bean = entity.getBean(ObjectBean.class);
        
        for (CategoryEntity catEntity: entity.getCategories()) {
            ObjectCategory oc = new ObjectCategory();
            Category category = catEntity.getBean(Category.class);
            oc.setCategory(category );
            for(ObjectDimensionValueEntity odve : entity.getDimensionValues()) {
                DimensionValueEntity dve = objectDao.getEntity(DimensionValueEntity.class, odve.getDimValue());
                DimensionEntity dimension = dve.getDimension();
                if(catEntity.hasDimension(dimension.getId())) {
                    oc.getDimValues().add(dve.getBean(DimensionValue.class));
                }
            }
            bean.getCategories().add(oc);
        }

        if (entity.getLocation() != null) {
            bean.setLocation(entity.getLocation().getBean(Location.class));
        }

        return bean;
    }

    @Override
    public ObjectBeanList search(String keywords, List<String> dimValueList, Float latitude, Float longitude,
            int range,
            int offset, int size) {
        List<ObjectEntity> entityList = objectDao.search(keywords, dimValueList, latitude, longitude, range,
                offset, size);
        ObjectBeanList result = new ObjectBeanList();
        for (ObjectEntity entity : entityList) {
            result.getList().add(getObjectBean(entity.getId()));
        }
        return result;
    }

    @Override
    public Category getCategoryByName(String name) {
        CategoryEntity entity = objectDao.getFirstEntityBy(CategoryEntity.class, "name", name);
        return entity == null ? null : getCategory(entity);
    }

    @Override
    public Category createChildCategories(Long catId, List<String> childNames) {
        CategoryEntity entity = objectDao.getEntity(CategoryEntity.class, catId);
        for (String name : childNames) {
            CategoryEntity childEntity = objectDao.getFirstEntityBy(CategoryEntity.class, "name", name);
            if (childEntity == null) {
                childEntity = new CategoryEntity();
                childEntity.setName(name);
                objectDao.saveEntity(childEntity);
            }
            if (!entity.contains(childEntity.getId())) {
                entity.getChildren().add(childEntity);
            }
            objectDao.saveEntity(entity);
        }
        return getCategory(entity);
    }
}
