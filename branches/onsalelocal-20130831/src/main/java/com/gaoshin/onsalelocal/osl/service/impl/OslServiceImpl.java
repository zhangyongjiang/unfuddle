package com.gaoshin.onsalelocal.osl.service.impl;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.api.DataSource;
import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.OfferDetails;
import com.gaoshin.onsalelocal.api.OfferDetailsList;
import com.gaoshin.onsalelocal.api.OfferList;
import com.gaoshin.onsalelocal.api.OfferSummary;
import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.api.StoreList;
import com.gaoshin.onsalelocal.api.StoreSource;
import com.gaoshin.onsalelocal.api.dao.StoreDao;
import com.gaoshin.onsalelocal.osl.dao.FavouriteDao;
import com.gaoshin.onsalelocal.osl.dao.OslDao;
import com.gaoshin.onsalelocal.osl.dao.UserDao;
import com.gaoshin.onsalelocal.osl.entity.FavouriteOffer;
import com.gaoshin.onsalelocal.osl.entity.FavouriteOfferDetails;
import com.gaoshin.onsalelocal.osl.entity.FavouriteOfferDetailsList;
import com.gaoshin.onsalelocal.osl.entity.FavouriteStore;
import com.gaoshin.onsalelocal.osl.entity.FavouriteStoreDetails;
import com.gaoshin.onsalelocal.osl.entity.FavouriteStoreDetailsList;
import com.gaoshin.onsalelocal.osl.entity.Follow;
import com.gaoshin.onsalelocal.osl.entity.FollowDetails;
import com.gaoshin.onsalelocal.osl.entity.FollowDetailsList;
import com.gaoshin.onsalelocal.osl.entity.Notification;
import com.gaoshin.onsalelocal.osl.entity.NotificationList;
import com.gaoshin.onsalelocal.osl.entity.OfferComment;
import com.gaoshin.onsalelocal.osl.entity.OfferCommentDetails;
import com.gaoshin.onsalelocal.osl.entity.OfferCommentDetailsList;
import com.gaoshin.onsalelocal.osl.entity.StoreComment;
import com.gaoshin.onsalelocal.osl.entity.StoreCommentDetails;
import com.gaoshin.onsalelocal.osl.entity.StoreCommentDetailsList;
import com.gaoshin.onsalelocal.osl.entity.StoreDetails;
import com.gaoshin.onsalelocal.osl.entity.StoreDetailsList;
import com.gaoshin.onsalelocal.osl.entity.Tag;
import com.gaoshin.onsalelocal.osl.entity.TagList;
import com.gaoshin.onsalelocal.osl.entity.User;
import com.gaoshin.onsalelocal.osl.entity.UserList;
import com.gaoshin.onsalelocal.osl.service.OslService;
import common.db.dao.City;
import common.db.dao.ConfigDao;
import common.db.dao.GeoDao;
import common.geo.Geocode;
import common.util.reflection.ReflectionUtil;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

@Service("oslService")
@Transactional(readOnly=true)
public class OslServiceImpl extends ServiceBaseImpl implements OslService {
    static final Logger logger = Logger.getLogger(OslServiceImpl.class);
    
	@Autowired private OslDao oslDao;
	@Autowired private GeoDao geoDao;
	@Autowired private StoreDao storeDao;
	@Autowired private UserDao userDao;
	@Autowired private FavouriteDao favouriteDao;
	@Autowired private ConfigDao configDao;

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public OfferDetailsList searchOffer(final float lat, final float lng, int radius, int offset, int size) {
		Set<String> address = new HashSet<String>();
		
		String source = com.gaoshin.onsalelocal.api.DataSource.Shoplocal.getValue();
		long t0 = System.currentTimeMillis();
		List<Offer> offers = oslDao.listOffersIn(lat, lng, radius, offset, 2000, source, null);
		long t1 = System.currentTimeMillis();
		logger.info("slocal search costs " + (t1-t0));
		
		if(offers.size() == 0) {
			List<City> cities = geoDao.nearbyCities(lat, lng, radius);
			for(City city : cities) {
				oslDao.ondemandShoplocal(city);
				break;
			}
		}
		
		OfferDetailsList list = new OfferDetailsList();
		for(Offer o : offers) {
			if(address.contains(o.getAddress()))
				continue;
			address.add(o.getAddress());
			OfferDetails details = ReflectionUtil.copy(OfferDetails.class, o);
			list.getItems().add(details);
			float dis1 = Geocode.distance(o.getLatitude(), o.getLongitude(), lat, lng);
			details.setDistance(dis1);
		}
		
		source = com.gaoshin.onsalelocal.api.DataSource.Yipit.getValue();
		t0 = System.currentTimeMillis();
		offers = oslDao.listOffersIn(lat, lng, radius, offset, size, source, null);
		t1 = System.currentTimeMillis();
		logger.info("yipit search costs " + (t1-t0));
		for(Offer o : offers) {
			if(address.contains(o.getAddress()))
				continue;
			address.add(o.getAddress());
			OfferDetails details = ReflectionUtil.copy(OfferDetails.class, o);
			list.getItems().add(details);
			float dis1 = Geocode.distance(o.getLatitude(), o.getLongitude(), lat, lng);
			details.setDistance(dis1);
		}
		
		Collections.sort(list.getItems(), new Comparator<OfferDetails>() {
			@Override
			public int compare(OfferDetails o1, OfferDetails o2) {
				return (int) ((o1.getDistance() - o2.getDistance()) * 1000);
			}
		});
		return list;
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public int harvest(String schema) {
		return oslDao.harvest(schema);
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void cleanup(int days) {
		oslDao.cleanup(days);
	}

	@Override
	public Offer getOffer(String offerId) {
		return oslDao.getEntity(Offer.class, offerId);
	}

	@Override
    public StoreList nearbyStores(Float latitude, Float longitude, Float radius, boolean hasOffer) {
	    return oslDao.nearbyStores(latitude, longitude, radius, hasOffer);
    }

	@Override
    public OfferDetailsList trend(String userId, Float latitude, Float longitude, Integer radius, int offset, int size) {
		assertNotNull(latitude);
		assertNotNull(longitude);
		assertNotNull(radius);
		
		List<Offer> items = oslDao.trend(latitude, longitude, radius, offset, size);
		if(items.size() < size) {
			List<Offer> more = oslDao.listOffersIn(latitude, longitude, radius, offset, size-items.size(), null, null);
			if(more != null)
				items.addAll(more);
		}
		
		OfferDetailsList list = new OfferDetailsList();
		list.setItems(decorateOffers(items, userId));
	    return list;
    }
	
	@Override
	public List<OfferDetails> decorateOffers(List<? extends Offer> offers, String userId) {
		List<FavouriteOffer> favs = favouriteDao.listByUserId(userId);
		List<OfferDetails> details = new ArrayList<OfferDetails>();
		for(Offer o : offers) {
			OfferDetails fd = (o instanceof OfferDetails) ? (OfferDetails)o : ReflectionUtil.copy(OfferDetails.class, o);
			for(FavouriteOffer fo : favs) {
				if(fo.getOfferId().equals(o.getId())) {
					fd.setLiked(true);
				}
			}
			details.add(fd);
		}
		return details;
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void follw(String userId, String followerId) {
		Follow follow = new Follow();
		follow.setCreated(System.currentTimeMillis());
		follow.setUserId(userId);
		follow.setFollowerId(followerId);
		follow.setUpdated(System.currentTimeMillis());
		oslDao.insert(follow);
    }

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public OfferDetails getOfferDetails(String userId, String offerId) {
		Offer offer = getOffer(offerId);
		OfferDetails details = ReflectionUtil.copy(OfferDetails.class, offer);
		if(DataSource.User.name().equals(offer.getSource())) {
			User user = userDao.getEntity(User.class, offer.getSourceId());
			details.setSubmitter(user);
		}
		
		if(userId != null) {
			details.setLiked(favouriteDao.isLiked(userId, offerId));
		}
		
		int updated = favouriteDao.update("update OfferSummary set views = views+1, popularity=popularity+1 where id=?", offerId);
		if(updated == 0) {
			OfferSummary summary = new OfferSummary();
			summary.setViews(1);
			oslDao.insert(summary);
		}
		
	    return details;
    }

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public FavouriteOffer like(String userId, String offerId) {
		FavouriteOffer like = new FavouriteOffer();
		like.setCreated(System.currentTimeMillis());
		like.setUserId(userId);
		like.setOfferId(offerId);
		favouriteDao.insert(like);
		
		int updated = favouriteDao.update("update Offer set likes = likes+1, popularity=popularity+1 where id=?", offerId);
		if(updated == 0) {
			OfferSummary summary = new OfferSummary();
			summary.setLikes(1);
			oslDao.insert(summary);
		}
		
	    return like;
    }

	@Override
    public UserList listLikedUserForOffer(String offerId) {
		List<User> users = favouriteDao.listLikedUsers(offerId);
		UserList list = new UserList();
		list.setItems(users);
	    return list;
    }

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public OfferComment comment(OfferComment comment) {
		assertNotNull(comment.getContent());
		assertNotNull(comment.getUserId());
		assertNotNull(comment.getOfferId());
		comment.setCreated(System.currentTimeMillis());
		oslDao.insert(comment);
		
		int updated = favouriteDao.update("update OfferSummary set likes = likes+1, popularity=popularity+1 where id=?", comment.getOfferId());
		if(updated == 0) {
			OfferSummary summary = new OfferSummary();
			summary.setComments(1);
			oslDao.insert(summary);
		}
		
	    return comment;
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public OfferCommentDetailsList listOfferComments(String userId, String offerId) {
		List<OfferComment> comments = oslDao.query(OfferComment.class, Collections.singletonMap("offerId", offerId));
		
		List<User> users = favouriteDao.listCommentedUsersForOffer(offerId);
		Map<String, User> userMap = new HashMap<String, User>();
		for(User u : users) {
			userMap.put(u.getId(), u);
		}
		
		OfferCommentDetailsList list = new OfferCommentDetailsList();
		for(OfferComment com : comments) {
			OfferCommentDetails details = ReflectionUtil.copy(OfferCommentDetails.class, com);
			list.getItems().add(details);
			details.setUser(userMap.get(com.getUserId()));
		}
		
		OfferDetails offerDetails = getOfferDetails(userId, offerId);
		list.setOfferDetails(offerDetails);
		
	    return list;
    }

	@Override
    public StoreDetails getStoreDetails(String storeId) {
		Store store = storeDao.getEntity(Store.class, storeId);
		int offers = storeDao.getOfferCount(storeId);
		int followers = storeDao.getFollowCount(storeId);
	    StoreDetails details = ReflectionUtil.copy(StoreDetails.class, store);
	    details.setOffers(offers);
	    details.setFollowers(followers);
		return details;
    }

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public FavouriteStore likeStore(String userId, String storeId) {
		FavouriteStore like = new FavouriteStore();
		like.setCreated(System.currentTimeMillis());
		like.setUserId(userId);
		like.setStoreId(storeId);
		favouriteDao.insert(like);
	    return like;
    }

	@Override
    public UserList listLikedUserForStore(String storeId) {
		List<User> users = favouriteDao.listLikedUsersForStore(storeId);
		UserList list = new UserList();
		list.setItems(users);
	    return list;
    }

	@Override
    public StoreComment commentStore(StoreComment comment) {
		assertNotNull(comment.getContent());
		assertNotNull(comment.getUserId());
		assertNotNull(comment.getStoreId());
		comment.setCreated(System.currentTimeMillis());
		oslDao.insert(comment);
	    return comment;
    }

	@Override
    public StoreCommentDetailsList listStoreComments(String storeId) {
		List<StoreComment> comments = oslDao.query(StoreComment.class, Collections.singletonMap("storeId", storeId));
		
		List<User> users = favouriteDao.listCommentedUsersForStore(storeId);
		Map<String, User> userMap = new HashMap<String, User>();
		for(User u : users) {
			userMap.put(u.getId(), u);
		}
		
		StoreCommentDetailsList list = new StoreCommentDetailsList();
		for(StoreComment com : comments) {
			StoreCommentDetails details = ReflectionUtil.copy(StoreCommentDetails.class, com);
			list.getItems().add(details);
			details.setUser(userMap.get(com.getUserId()));
		}
		
		StoreDetails storeDetails = getStoreDetails(storeId);
		list.setStoreDetails(storeDetails);
		
	    return list;
    }

	@Override
    public OfferList listStoreOffers(String storeId) {
		List<Offer> items = oslDao.listStoreOffers(storeId);
		OfferList list = new OfferList(items);
	    return list;
    }

	@Override
    public FollowDetailsList followers(String userId) {
		List<Follow> list = oslDao.query(Follow.class, Collections.singletonMap("userId", userId));
		User user = userDao.getEntity(User.class, userId);
		
		String sql = "select u.* from User u, Follow f where f.followerId=u.id and f.userId=:userId";
		List<User> users = oslDao.queryBySql(User.class, Collections.singletonMap("userId", userId), sql );
		Map<String, User> userMap = new HashMap<String, User>();
		for(User u : users) {
			userMap.put(u.getId(), u);
		}
		
		FollowDetailsList detailsList = new FollowDetailsList();
		detailsList.setUser(user);
		for(Follow f : list) {
			FollowDetails d = ReflectionUtil.copy(FollowDetails.class, f);
			d.setUser(userMap.get(f.getFollowerId()));
			detailsList.getItems().add(d);
		}
		detailsList.setUser(user);
		
	    return detailsList;
    }

	@Override
    public FollowDetailsList followings(String userId) {
		List<Follow> list = oslDao.query(Follow.class, Collections.singletonMap("followerId", userId));
		User user = userDao.getEntity(User.class, userId);
		
		String sql = "select u.* from User u, Follow f where f.userId=u.id and f.followerId=:userId";
		List<User> users = oslDao.queryBySql(User.class, Collections.singletonMap("userId", userId), sql );
		Map<String, User> userMap = new HashMap<String, User>();
		for(User u : users) {
			userMap.put(u.getId(), u);
		}
		
		FollowDetailsList detailsList = new FollowDetailsList();
		detailsList.setUser(user);
		for(Follow f : list) {
			FollowDetails d = ReflectionUtil.copy(FollowDetails.class, f);
			d.setUser(userMap.get(f.getFollowerId()));
			detailsList.getItems().add(d);
		}
		detailsList.setUser(user);
		
	    return detailsList;
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void submitOffer(Offer offer) {
		if(offer.getId() != null) {
			Offer db = oslDao.getEntity(Offer.class, offer.getId());
			if(db != null && !db.getSourceId().equals(offer.getSourceId())) {
				throw new BusinessException(ServiceError.Forbidden, "not the original owner of the offer");
			}
		}
		assertNotNull(offer.getTitle(), "title");
		assertNotNull(offer.getAddress());
		assertNotNull(offer.getCity());
		assertNotNull(offer.getState());
		assertNotNull(offer.getCountry());
		assertNotNull(offer.getLatitude());
		assertNotNull(offer.getLongitude());
		assertNotNull(offer.getMerchant());
		assertNotNull(offer.getSource());
		
		if(offer.getMerchantId() == null && offer.getLatitude() != null) {
			Store store = new Store();
			if(DataSource.User.name().equals(offer.getSource())) {
				store.setOwnerUserId(offer.getSourceId());
			}
			store.setAddress(offer.getAddress());
			store.setCity(offer.getCity());
			store.setCountry(offer.getCountry());
			store.setState(offer.getState());
			store.setPhone(offer.getPhone());
			store.setName(offer.getMerchant());
			store.setId(offer.getMerchantId());
			store.setLatitude(new BigDecimal(offer.getLatitude()));
			store.setLongitude(new BigDecimal(offer.getLongitude()));
			Store db = oslDao.searchStore(null, null, store);
			if(db == null) {
				store.setOffers(1);
				store = createStore(store.getOwnerUserId(), store);
			}
			else {
				store.setId(db.getId());
			}
			offer.setMerchantId(store.getId());
		}
		else {
			storeDao.incOfferCount(offer.getMerchantId());
		}
		
		oslDao.replace(offer);
    }

	@Override
    public FavouriteOfferDetailsList listOffersLikedByUser(String userId) {
		List<FavouriteOffer> favs = favouriteDao.listByUserId(userId);
		List<Offer> offers = favouriteDao.listUserLikedOffers(userId);
		Map<String, Offer> map = new HashMap<String, Offer>();
		for(Offer o : offers) {
			map.put(o.getId(), o);
		}
		FavouriteOfferDetailsList list = new FavouriteOfferDetailsList();
		for(FavouriteOffer fo : favs) {
			FavouriteOfferDetails d = ReflectionUtil.copy(FavouriteOfferDetails.class, fo);
			d.setOffer(map.get(fo.getOfferId()));
			list.getItems().add(d);
		}
	    return list;
    }

	@Override
    public FavouriteStoreDetailsList listStoresLikedByUser(String userId) {
		List<Store> offers = favouriteDao.listUserLikedStores(userId);
		FavouriteStoreDetailsList list = new FavouriteStoreDetailsList();
		for(Store store : offers) {
			FavouriteStoreDetails d = ReflectionUtil.copy(FavouriteStoreDetails.class, store);
			d.setStore(store);
			list.getItems().add(d);
		}
	    return list;
    }

	@Override
    public OfferDetailsList listOffersCreatedByUser(String userId) {
		List<Offer> offers = userDao.listOffersCreatedByUser(userId);
		OfferDetailsList list = new OfferDetailsList();
		for(Offer o : offers) {
			OfferDetails d = ReflectionUtil.copy(OfferDetails.class, o);
			list.getItems().add(d);
		}
	    return list;
    }

	public OslDao getOslDao() {
	    return oslDao;
    }

	public void setOslDao(OslDao oslDao) {
	    this.oslDao = oslDao;
    }

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public Store createStore(String userId, Store store) {
		store.setOwnerUserId(userId);
		assertNotNull(store.getAddress(), "address");
		assertNotNull(store.getCity(), "city");
		assertNotNull(store.getCountry(), "country");
		assertNotNull(store.getLatitude(), "latitude");
		assertNotNull(store.getLongitude(), "longitude");
		assertNotNull(store.getName(), "name");
		assertNotNull(store.getState(), "state");
		storeDao.insert(store);
	    return store;
    }

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void updateStoreImageUrl(String storeId, String imgUrl) {
		storeDao.updateStoreImageUrl(storeId, imgUrl);
    }

	@Override
    public StoreDetailsList listStoresCreatedByUser(String userId) {
		List<Store> offers = userDao.listStoresCreatedByUser(userId);
		StoreDetailsList list = new StoreDetailsList();
		for(Store o : offers) {
			StoreDetails d = ReflectionUtil.copy(StoreDetails.class, o);
			list.getItems().add(d);
		}
	    return list;
    }

	@Override
    public Store updateStore(String userId, Store store) {
		assertNotNull(store.getId());
		Store dbstore = storeDao.getEntity(Store.class, store.getId());
		if(!userId.equals(dbstore.getOwnerUserId())) 
				throw new BusinessException(ServiceError.NotAuthorized);
		store.setChainStoreId(dbstore.getChainStoreId());
		store.setOwnerUserId(userId);
		store.setParentId(dbstore.getParentId());
		storeDao.updateEntity(store);
	    return store;
    }

	@Override
    public TagList listTags() {
		TagList tags = new TagList();
		List<Tag> list = oslDao.listTags();
		tags.setItems(list);
		return tags;
    }

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void deleteOffer(String offerId) {
		oslDao.update("update Store s, Offer o set s.offers=s.offers-1 where o.id=? and o.merchantId=s.id", offerId);
		oslDao.update("delete from Offer where id=?", offerId);
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public int importYipitOffer(int batchSize) {
		String uuid = UUID.randomUUID().toString();
		String sql = "update yipit.Offer set status=? where status is null limit ?";
		oslDao.update(sql, uuid, batchSize);
		sql = "select * from yipit.Offer where status=?";
		List<Map<String, String>> list = oslDao.getJdbcTemplate().query(sql, new Object[]{uuid}, new RowMapper<Map<String, String>>(){
			@Override
            public Map<String, String> mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
				Map<String, String> row = new HashMap<String, String>();
				ResultSetMetaData metaData = rs.getMetaData();
				for(int i=0;i<metaData.getColumnCount(); i++) {
					String name = metaData.getColumnName(i+1);
					Object value = rs.getObject(i+1);
					row.put(name, value==null ? null : value.toString());
				}
	            return row;
            }
		});
		
		for(Map<String, String> row : list) {
			String merchantId = row.get("merchantId");
			Store store = storeDao.getStoreBySourceId(DataSource.Yipit, merchantId);
			if(store == null) {
				store = new Store();
				store.setAddress(row.get("address"));
				store.setCity(row.get("city"));
				store.setState(row.get("state"));
				store.setCountry("US");
				store.setLatitude(new BigDecimal(row.get("latitude")));
				store.setLongitude(new BigDecimal(row.get("longitude")));
				store.setPhone(row.get("phone"));
				store.setName(row.get("merchant"));
				store.setWeb(row.get("merchantLogo"));
				store.setOffers(1);
				createStore(null, store);
				
				StoreSource ss = new StoreSource();
				ss.setSource(DataSource.Yipit);
				ss.setSourceId(merchantId);
				ss.setStoreId(store.getId());
				storeDao.replace(ss);
			}
			else {
				storeDao.incOfferCount(store.getId());
			}
			
			Offer offer = new Offer();
			offer.setMerchantId(store.getId());
			offer.setAddress(row.get("address"));
			offer.setCity(row.get("city"));
			offer.setCountry("US");
			offer.setDescription(row.get("description"));
			offer.setEnd(Long.parseLong(row.get("end")));
			offer.setStart(Long.parseLong(row.get("start")));
			offer.setDiscount(row.get("discount"));
			offer.setHighlights(row.get("highlights"));
			offer.setLargeImg(row.get("largeImg"));
			offer.setLatitude(Float.parseFloat(row.get("latitude")));
			offer.setLongitude(Float.parseFloat(row.get("longitude")));
			offer.setMerchant(row.get("merchant"));
			offer.setPhone(row.get("phone"));
			offer.setPrice(row.get("price"));
			offer.setRootSource(row.get("rootSource"));
			offer.setSmallImg(row.get("smallImg"));
			offer.setSource(DataSource.Yipit.name());
			offer.setState(row.get("state"));
			offer.setTags(row.get("tags"));
			offer.setTitle(row.get("title"));
			offer.setZipcode(row.get("zipcode"));
			
			try {
	            BufferedImage bimg = ImageIO.read(new URL(offer.getLargeImg()));
	            offer.setWidth(bimg.getWidth());
	            offer.setHeight(bimg.getHeight());
            } catch (Exception e) {
	            e.printStackTrace();
            }
			
			oslDao.replace(offer);
		}
		
		return list.size();
	}

	@Override
    public NotificationList listUserNotifications(String userId, int offset, int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("offset", offset);
		params.put("size", size);
		List<Notification> list = oslDao.queryBySql(Notification.class, params, "select * from Notification where userId=:userId order by created desc limit :offset, :size");
	    NotificationList notificationList = new NotificationList();
	    notificationList.setItems(list);
		return notificationList;
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void unlikeStore(String userId, String storeId) {
		favouriteDao.update("delete from FavouriteStore where userId=? and storeId=?", userId, storeId);
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void unlike(String userId, String offerId) {
		favouriteDao.update("delete from FavouriteOffer where userId=? and offerId=?", userId, offerId);
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void unfollow(String userId, String followerId) {
		oslDao.update("delete from Follow where userId=? and followerId=?", userId, followerId);
    }
}
