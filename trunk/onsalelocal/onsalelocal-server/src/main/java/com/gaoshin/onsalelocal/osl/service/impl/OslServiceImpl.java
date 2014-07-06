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

import com.gaoshin.onsalelocal.osl.dao.FavouriteDao;
import com.gaoshin.onsalelocal.osl.dao.OslDao;
import com.gaoshin.onsalelocal.osl.dao.UserDao;
import com.gaoshin.onsalelocal.osl.service.OslService;
import com.nextshopper.api.Company;
import com.nextshopper.api.CompanyAddress;
import com.nextshopper.api.DataSource;
import com.nextshopper.api.Deal;
import com.nextshopper.api.Offer;
import com.nextshopper.api.OfferDetails;
import com.nextshopper.api.OfferDetailsList;
import com.nextshopper.api.OfferStatus;
import com.nextshopper.api.Store;
import com.nextshopper.api.StoreList;
import com.nextshopper.api.StoreSource;
import com.nextshopper.api.UserOfferDetailsList;
import com.nextshopper.api.dao.CategoryDao;
import com.nextshopper.api.dao.StoreDao;
import com.nextshopper.osl.entity.AccountStatus;
import com.nextshopper.osl.entity.FavouriteOffer;
import com.nextshopper.osl.entity.FavouriteOfferDetails;
import com.nextshopper.osl.entity.FavouriteOfferDetailsList;
import com.nextshopper.osl.entity.FavouriteStore;
import com.nextshopper.osl.entity.FavouriteStoreDetails;
import com.nextshopper.osl.entity.FavouriteStoreDetailsList;
import com.nextshopper.osl.entity.Follow;
import com.nextshopper.osl.entity.FollowDetails;
import com.nextshopper.osl.entity.FollowDetailsList;
import com.nextshopper.osl.entity.FollowStatus;
import com.nextshopper.osl.entity.Notification;
import com.nextshopper.osl.entity.NotificationDetail;
import com.nextshopper.osl.entity.NotificationDetailList;
import com.nextshopper.osl.entity.OfferComment;
import com.nextshopper.osl.entity.OfferCommentDetails;
import com.nextshopper.osl.entity.OfferCommentDetailsList;
import com.nextshopper.osl.entity.StoreComment;
import com.nextshopper.osl.entity.StoreCommentDetails;
import com.nextshopper.osl.entity.StoreCommentDetailsList;
import com.nextshopper.osl.entity.StoreDetails;
import com.nextshopper.osl.entity.StoreDetailsList;
import com.nextshopper.osl.entity.Tag;
import com.nextshopper.osl.entity.TagList;
import com.nextshopper.osl.entity.User;
import com.nextshopper.osl.entity.UserDetails;
import com.nextshopper.osl.entity.UserDetailsList;
import com.nextshopper.osl.entity.UserList;

import common.crawler.CrawlerBase;
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
	@Autowired private CategoryDao categoryDao;

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public OfferDetailsList searchOffer(final float lat, final float lng, int radius, int offset, int size) {
		Set<String> address = new HashSet<String>();
		
		String source = com.nextshopper.api.DataSource.Shoplocal.getValue();
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
		
		source = com.nextshopper.api.DataSource.Yipit.getValue();
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
	public Offer getCompanyOffer(String offerId) {
		String sql = "select * from CompanyDeal where id=:id";
		return oslDao.queryUniqueBySql(Offer.class, Collections.singletonMap("id", offerId), sql);
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
		
		for(OfferDetails od : list.getItems()) {
			if(latitude != null && longitude != null && od.getLatitude() != null && od.getLongitude()!=null) 
				od.setDistance(Geocode.distance(latitude, longitude, od.getLatitude(), od.getLongitude()));
		}
		
	    return list;
    }

	@Override
    public OfferDetailsList listOfferDetailByIds(String userId, List<String> ids, Float latitude, Float longitude) {
		List<Offer> items = oslDao.getOfferList(ids);
		OfferDetailsList list = new OfferDetailsList();
		list.setItems(decorateOffers(items, userId));
		
		for(OfferDetails od : list.getItems()) {
			if(latitude != null && longitude != null && od.getLatitude() != null && od.getLongitude()!=null) 
				od.setDistance(Geocode.distance(latitude, longitude, od.getLatitude(), od.getLongitude()));
		}
		
	    return list;
    }
	
	@Override
	public List<OfferDetails> decorateOffers(List<? extends Offer> offers, String userId) {
		List<String> userIds = new ArrayList<String>();
		for(Offer o : offers) {
			if("User".equals(o.getSource())) {
				userIds.add(o.getSourceId());
			}
		}
		Map<String, User> userMap = userDao.getUserMap(userIds);
		
		Set<String> followerIds = new HashSet<String>();
		if(userId != null) {
			List<Follow> followers = oslDao.query(Follow.class, Collections.singletonMap("userId", userId));
			for(Follow f : followers) {
				followerIds.add(f.getFollowerId());
			}
		}
		
		Set<String> followingIds = new HashSet<String>();
		if(userId != null) {
			List<Follow> followings = oslDao.query(Follow.class, Collections.singletonMap("followerId", userId));
			for(Follow f : followings) {
				followingIds.add(f.getUserId());
			}
		}
		
		List<FavouriteOffer> favs = new ArrayList<FavouriteOffer>();
		if(userId != null) {
			favs = favouriteDao.listByUserId(userId);
		}
		List<OfferDetails> details = new ArrayList<OfferDetails>();
		for(Offer o : offers) {
			OfferDetails fd = (o instanceof OfferDetails) ? (OfferDetails)o : ReflectionUtil.copy(OfferDetails.class, o);
			if("User".equalsIgnoreCase(fd.getSource())) {
				User user = userMap.get(fd.getSourceId());
				if(user != null) {
					UserDetails submitter = ReflectionUtil.copy(UserDetails.class, user);
//					submitter.setMyFollower(followerIds.contains(user.getId()));
					submitter.setMyFollowing(followingIds.contains(user.getId()));
					fd.setSubmitter(submitter);
				}
			}
			
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
    public Follow follw(String userId, String followerId) {
		if(isFollowing(followerId, userId))
			throw new BusinessException(ServiceError.Duplicated, "user " +  followerId + " is alredy following " + userId);
		Follow follow = new Follow();
		follow.setCreated(System.currentTimeMillis());
		follow.setUserId(userId);
		follow.setFollowerId(followerId);
		follow.setUpdated(System.currentTimeMillis());
		oslDao.insert(follow);
		return follow;
    }

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public OfferDetails getOfferDetails(String userId, String offerId) {
		Offer offer = getCompanyOffer(offerId);
		OfferDetails details = ReflectionUtil.copy(OfferDetails.class, offer);
		if(DataSource.User.name().equals(offer.getSource())) {
			UserDetails user = userDao.getUserDetails(offer.getSourceId());
			details.setSubmitter(user);
			if(userId != null) {
            	boolean following = isFollowing(userId, details.getSubmitter().getId());
            	user.setMyFollowing(following);
//            	boolean followed = isFollowing(details.getSubmitter().getId(), userId);
//            	user.setMyFollower(followed);
			}
		}
		
		if(userId != null) {
			details.setLiked(favouriteDao.isLiked(userId, offerId));
		}
		
	    return details;
    }

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public FavouriteOffer like(String userId, String offerId) {
		if(favouriteDao.isLiked(userId, offerId))
			throw new BusinessException(ServiceError.Duplicated, "user " + userId + " liked offer " + offerId + " already.");
		FavouriteOffer like = new FavouriteOffer();
		like.setCreated(System.currentTimeMillis());
		like.setUserId(userId);
		like.setOfferId(offerId);
		favouriteDao.insert(like);
		
		int updated = favouriteDao.update("update Offer set likes = likes+1, popularity=popularity+10 where id=?", offerId);
		
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
		
		int updated = favouriteDao.update("update Offer set comments = comments+1, popularity=popularity+1 where id=?", comment.getOfferId());
		
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
    public FavouriteStore followStore(String userId, String storeId) {
		if(isFollowingStore(storeId, userId)) {
			throw new BusinessException(ServiceError.Duplicated, "user " + userId + " followed store " + storeId + " already");
		}
		FavouriteStore like = new FavouriteStore();
		like.setCreated(System.currentTimeMillis());
		like.setUserId(userId);
		like.setStoreId(storeId);
		favouriteDao.insert(like);
	    return like;
    }

	@Override
    public UserDetailsList listLikedUserForStore(String storeId, String userId) {
		List<User> users = favouriteDao.listLikedUsersForStore(storeId);
		UserDetailsList list = new UserDetailsList();
		Map<String, User> userMap = oslDao.followingUserMap(userId);
		for(User u : users) {
			UserDetails ud = ReflectionUtil.copy(UserDetails.class, u);
			if(userMap.containsKey(u.getId())) {
				ud.setMyFollowing(true);
			}
			list.getItems().add(ud);
		}
	    return list;
    }

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
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
    public OfferDetailsList listStoreOffers(String storeId, String userId, int offset, int size) {
		List<Offer> items = oslDao.listStoreOffers(storeId, offset, size);
		OfferDetailsList list = new OfferDetailsList();
		List<OfferDetails> decorateOffers = decorateOffers(items, userId);
		list.setItems(decorateOffers);
	    return list;
    }

	@Override
    public FollowDetailsList followers(String userId, String currentUserId) {
		List<Follow> list = oslDao.query(Follow.class, Collections.singletonMap("userId", userId));
		User user = userDao.getEntity(User.class, userId);
		
		List<User> users = oslDao.listFollowers(userId);
		Map<String, User> userMap = new HashMap<String, User>();
		for(User u : users) {
			userMap.put(u.getId(), u);
		}
		
		List<Follow> followings = oslDao.query(Follow.class, Collections.singletonMap("followerId", currentUserId));
		Set<String> followingIds = new HashSet<String>();
		for(Follow f : followings) {
			followingIds.add(f.getUserId());
		}
		
		FollowDetailsList detailsList = new FollowDetailsList();
		detailsList.setUser(user);
		for(Follow f : list) {
			FollowDetails d = ReflectionUtil.copy(FollowDetails.class, f);
			d.setMyFollowing(followingIds.contains(f.getFollowerId()));
			d.setUser(userMap.get(f.getFollowerId()));
			detailsList.getItems().add(d);
		}
		
	    return detailsList;
    }

	@Override
    public FollowDetailsList followings(String userId, String currentUserId) {
		List<Follow> list = oslDao.query(Follow.class, Collections.singletonMap("followerId", userId));
		User user = userDao.getEntity(User.class, userId);
		
		Map<String, User> userMap = oslDao.followingUserMap(userId);
		Map<String, User> myMap = oslDao.followingUserMap(currentUserId);
		
		FollowDetailsList detailsList = new FollowDetailsList();
		detailsList.setUser(user);
		for(Follow f : list) {
			FollowDetails d = ReflectionUtil.copy(FollowDetails.class, f);
			d.setUser(userMap.get(f.getUserId()));
			d.setMyFollowing(myMap.containsKey(f.getUserId()));
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
		assertNotNull(offer.getSource(), "source");
		assertNotNull(offer.getTags(), "tags");
		if(offer.getPhone()!=null)
			offer.setPhone(CrawlerBase.formatPhone(offer.getPhone()));
		
		offer.setTags(categoryDao.checkTags(offer.getTags()));
		
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
				store = createStore(store.getOwnerUserId(), store);
			}
			else {
				store.setId(db.getId());
			}
			offer.setMerchantId(store.getId());
		}
		
		Deal deal = ReflectionUtil.copy(Deal.class, offer);
		oslDao.replace(deal);
		offer.setId(deal.getId());
    }

	@Override
    public FavouriteOfferDetailsList listOffersLikedByUser(String userId, String currentUserId) {
		List<FavouriteOffer> favs = favouriteDao.listByUserId(userId);
		List<Offer> offers = favouriteDao.listUserLikedOffers(userId);
		List<OfferDetails> offerDetailList = decorateOffers(offers, currentUserId);
		
		Map<String, OfferDetails> map = new HashMap<String, OfferDetails>();
		for(OfferDetails o : offerDetailList) {
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
    public FavouriteStoreDetailsList listStoresLikedByUser(String userId, String currentUserId) {
		List<Store> offers = favouriteDao.listUserLikedStores(userId);
		
		Set<String> myFollowingStoreIds = new HashSet<String>();
		if(currentUserId != null){
			List<Store> myFollowingStores = favouriteDao.listUserLikedStores(currentUserId);
			for(Store s : myFollowingStores) {
				myFollowingStoreIds.add(s.getId());
			}
		}
		
		FavouriteStoreDetailsList list = new FavouriteStoreDetailsList();
		for(Store store : offers) {
			FavouriteStoreDetails d = ReflectionUtil.copy(FavouriteStoreDetails.class, store);
			d.setStore(store);
			d.setMyFollowingStore(myFollowingStoreIds.contains(store.getId()));
			list.getItems().add(d);
		}
	    return list;
    }

	@Override
    public OfferDetailsList listOffersCreatedByUser(String userId, int offset, int size, String currentUserId) {
		Set<String> mylikeids = new HashSet<String>();
		if(currentUserId != null){
			logger.info("favouriteDao.listByUserId");
			List<FavouriteOffer> favOffers = favouriteDao.listByUserId(currentUserId);
			for(FavouriteOffer o : favOffers) {
				mylikeids.add(o.getOfferId());
			}
		}
		
		logger.info("listOffersCreatedByUser");
		List<Offer> offers = userDao.listOffersCreatedByUser(userId, offset, size);
		OfferDetailsList list = new OfferDetailsList();
		for(Offer o : offers) {
			OfferDetails d = ReflectionUtil.copy(OfferDetails.class, o);
			d.setLiked(mylikeids.contains(o.getId()));
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
		if(store.getPhone()!=null)
			store.setPhone(CrawlerBase.formatPhone(store.getPhone()));
		
		Company company = ReflectionUtil.copy(Company.class, store);
		storeDao.insert(company);
		store.setId(company.getId());
		
		CompanyAddress ca = ReflectionUtil.copy(CompanyAddress.class, store);
		ca.setId(null);
		storeDao.insert(ca);
		
	    return store;
    }

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void updateStoreImageUrl(String storeId, String imgUrl) {
		storeDao.updateStoreImageUrl(storeId, imgUrl);
    }

	@Override
    public StoreDetailsList listStoresCreatedByUser(String userId, String currentUserId) {
		Set<String> myFollowingStoreIds = new HashSet<String>();
		{
			List<Store> myFollowingStores = favouriteDao.listUserLikedStores(currentUserId);
			for(Store s : myFollowingStores) {
				myFollowingStoreIds.add(s.getId());
			}
		}
		
		List<Store> offers = userDao.listStoresCreatedByUser(userId);
		StoreDetailsList list = new StoreDetailsList();
		for(Store o : offers) {
			StoreDetails d = ReflectionUtil.copy(StoreDetails.class, o);
			d.setFollowing(myFollowingStoreIds.contains(o.getId()));
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
		store.setCompanyId(dbstore.getCompanyId());
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
				createStore(null, store);
				
				StoreSource ss = new StoreSource();
				ss.setSource(DataSource.Yipit);
				ss.setSourceId(merchantId);
				ss.setStoreId(store.getId());
				storeDao.replace(ss);
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
			offer.setTags(row.get("tags").replace('\n', ';'));
			offer.setTitle(row.get("title"));
			offer.setZipcode(row.get("zipcode"));
			offer.setUrl(row.get("url"));
			
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
    public NotificationDetailList listUserNotifications(String userId, int offset, int size) {
		List<Notification> list = oslDao.getLatestNotifications(userId, offset, size);
		List<String> userIds = new ArrayList<String>();
		List<String> offerIds = new ArrayList<String>();
		List<String> storeIds = new ArrayList<String>();
		for(Notification noti : list) {
			if(noti.getEventUserId() != null)
				userIds.add(noti.getEventUserId());
			if(noti.getOfferId() != null)
				offerIds.add(noti.getOfferId());
			if(noti.getStoreId() != null)
				storeIds.add(noti.getStoreId());
		}
		Map<String, User> userMap = userDao.getUserMap(userIds);
		Map<String, Store> storeMap = oslDao.getStoreMap(storeIds);
		Map<String, Offer> offerMap = oslDao.getOfferMap(offerIds);
		
	    NotificationDetailList notificationList = new NotificationDetailList();
	    for(Notification noti : list) {
	    	NotificationDetail nd = ReflectionUtil.copy(NotificationDetail.class, noti);
	    	nd.setUser(userMap.get(noti.getEventUserId()));
	    	nd.setOffer(offerMap.get(noti.getOfferId()));
	    	nd.setStore(storeMap.get(noti.getStoreId()));
	    	notificationList.getItems().add(nd);
	    }
	    
	    int totalMsg = oslDao.getTotalNotifications(userId);
	    int totalUnread  = oslDao.getTotalUnreadNotifications(userId);
	    notificationList.setTotalMsg(totalMsg);
	    notificationList.setTotalUnread(totalUnread);
	    
		return notificationList;
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void unfollowStore(String userId, String storeId) {
		favouriteDao.update("delete from FavouriteStore where userId=? and storeId=?", userId, storeId);
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void unlike(String userId, String offerId) {
		int updated = favouriteDao.update("delete from FavouriteOffer where userId=? and offerId=?", userId, offerId);
		if(updated > 0) {
			updated = favouriteDao.update("update Offer set likes = likes - 1, popularity=popularity-10 where id=?", offerId);
		}
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void unfollow(String userId, String followerId) {
		oslDao.update("delete from Follow where userId=? and followerId=?", userId, followerId);
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void copyOffer(Offer offer) {
		Offer copy = ReflectionUtil.copy(offer);
		List<Store> chains = storeDao.getChainStores(offer.getMerchantId());
		for(Store s : chains) {
			if(s.getId().equals(offer.getMerchantId()))
				continue;
			copy.setId(null);
			copy.setAddress(s.getAddress());
			copy.setCity(s.getCity());
			copy.setState(s.getState());
			copy.setLatitude(s.getLatitude().floatValue());
			copy.setLongitude(s.getLongitude().floatValue());
			copy.setCountry(s.getCountry());
			copy.setMerchantId(s.getId());
			if(s.getPhone() != null)
				copy.setPhone(s.getPhone());
			oslDao.insert(copy);
		}
    }

	@Override
    public boolean isFollowing(String ua, String ub) {
		List<Follow> list = oslDao.queryBySql(Follow.class, "select * from Follow where userId=? and followerId=?", ub, ua);
	    return list.size() > 0;
    }

	@Override
    public UserOfferDetailsList followingsDeals(String userId, int offset, int size) {
		List<Offer> items = oslDao.followingsDeals(userId, offset, size);
		UserOfferDetailsList list = new UserOfferDetailsList();
		list.setItems(decorateOffers(items, userId));
		UserDetails user = userDao.getUserDetails(userId);
		list.setUserDetails(user);
	    return list;
    }

	@Override
    public boolean isFollowingStore(String storeId, String userId) {
		List<FavouriteStore> list = oslDao.queryBySql(FavouriteStore.class, "select * from FavouriteStore where userId=? and storeId=?", userId, storeId);
	    return list.size() > 0;
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void flagUser(String meId, String userId) {
		userDao.update("update User set status=? where id=?", AccountStatus.Flagged.name(), userId);
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void flagOffer(String meId, String offerId) {
		oslDao.update("update Offer set status=? where id=?", OfferStatus.Flagged.name(), offerId);
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void markAllNotificationsRead(String userId) {
		oslDao.markAllNotificationsRead(userId);
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public Company createCompany(String userId, Company company) {
		User user = userDao.getEntity(User.class, userId);
		if(!AccountStatus.Super.equals(user.getStatus())) {
			throw new BusinessException(ServiceError.NotAuthorized);
		}
		storeDao.insert(company);
	    return company;
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public Company updateCompany(String userId, Company company) {
		User user = userDao.getEntity(User.class, userId);
		if(!AccountStatus.Super.equals(user.getStatus())) {
			throw new BusinessException(ServiceError.NotAuthorized);
		}
		Company db = storeDao.getEntity(Company.class, company.getId());
		if(db == null) {
			throw new BusinessException(ServiceError.NotFound);
		}
		storeDao.updateEntity(company);
	    return company;
    }

	@Override
    public Company getCompany(String companyId) {
	    return oslDao.getEntity(Company.class, companyId);
    }

	@Override
    public List<Company> listCompanies(int offset, int size) {
		String sql = "select * from Company where created > 0";
	    return oslDao.queryBySql(Company.class, sql);
    }

	@Override
    public List<Company> searchCompanies(String keywords, int offset, int size) {
		keywords = keywords + "%";
		String sql = "select * from Company where name like (:keywords)";
	    return oslDao.queryBySql(Company.class, sql, keywords);
    }

	@Override
    public void follow(String followerId, String userId, FollowStatus status) {
		Follow follow = follw(userId, followerId);
		follow.setStatus(status);
		oslDao.updateEntity(follow);
    }
}
