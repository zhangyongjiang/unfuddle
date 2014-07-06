package com.gaoshin.onsalelocal.osl.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.api.DataSource;
import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.OfferDetails;
import com.gaoshin.onsalelocal.api.OfferDetailsList;
import com.gaoshin.onsalelocal.api.OfferList;
import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.api.StoreList;
import com.gaoshin.onsalelocal.api.dao.StoreDao;
import com.gaoshin.onsalelocal.osl.dao.FavouriteDao;
import com.gaoshin.onsalelocal.osl.dao.OslDao;
import com.gaoshin.onsalelocal.osl.dao.UserDao;
import com.gaoshin.onsalelocal.osl.entity.FavouriteOffer;
import com.gaoshin.onsalelocal.osl.entity.FavouriteStore;
import com.gaoshin.onsalelocal.osl.entity.Follow;
import com.gaoshin.onsalelocal.osl.entity.FollowDetails;
import com.gaoshin.onsalelocal.osl.entity.FollowDetailsList;
import com.gaoshin.onsalelocal.osl.entity.OfferComment;
import com.gaoshin.onsalelocal.osl.entity.OfferCommentDetails;
import com.gaoshin.onsalelocal.osl.entity.OfferCommentDetailsList;
import com.gaoshin.onsalelocal.osl.entity.StoreComment;
import com.gaoshin.onsalelocal.osl.entity.StoreCommentDetails;
import com.gaoshin.onsalelocal.osl.entity.StoreCommentDetailsList;
import com.gaoshin.onsalelocal.osl.entity.StoreDetails;
import com.gaoshin.onsalelocal.osl.entity.User;
import com.gaoshin.onsalelocal.osl.entity.UserList;
import com.gaoshin.onsalelocal.osl.service.OslService;
import common.db.dao.City;
import common.db.dao.GeoDao;
import common.geo.Geocode;
import common.util.reflection.ReflectionUtil;

@Service("oslService")
@Transactional(readOnly=true)
public class OslServiceImpl extends ServiceBaseImpl implements OslService {
    static final Logger logger = Logger.getLogger(OslServiceImpl.class);
    
	@Autowired private OslDao oslDao;
	@Autowired private GeoDao geoDao;
	@Autowired private StoreDao storeDao;
	@Autowired private UserDao userDao;
	@Autowired private FavouriteDao favouriteDao;

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public OfferDetailsList searchOffer(final float lat, final float lng, int radius, int offset, int size) {
		Set<String> address = new HashSet<String>();
		
		String source = com.gaoshin.onsalelocal.api.DataSource.Shoplocal.getValue();
		long t0 = System.currentTimeMillis();
		List<Offer> offers = oslDao.listOffersIn(lat, lng, radius, offset, 2000, source);
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
		offers = oslDao.listOffersIn(lat, lng, radius, offset, size, source);
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
    public StoreList nearbyStores(Float latitude, Float longitude, Float radius, String category, String subcategory, Boolean serviceOnly) {
	    return oslDao.nearbyStores(latitude, longitude, radius, category, subcategory, serviceOnly);
    }

	@Override
    public OfferList trend(String userId, Float latitude, Float longitude, Integer radius, int offset, int size) {
		List<Offer> items = oslDao.listOffersIn(latitude, longitude, radius, offset, size, null);
		OfferList list = new OfferList(items);
	    return list;
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
    public OfferDetails getOfferDetails(String offerId) {
		Offer offer = getOffer(offerId);
		OfferDetails details = ReflectionUtil.copy(OfferDetails.class, offer);
		if(DataSource.User.name().equals(offer.getSource())) {
			User user = userDao.getEntity(User.class, offer.getSourceId());
			details.setSubmitter(user);
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
		comment.setCreated(System.currentTimeMillis());
		oslDao.insert(comment);
	    return comment;
    }

	@Override
    public OfferCommentDetailsList listOfferComments(String offerId) {
		List<OfferComment> comments = oslDao.query(OfferComment.class, Collections.singletonMap("offerId", offerId));
		
		List<User> users = favouriteDao.listLikedUsers(offerId);
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
		comment.setCreated(System.currentTimeMillis());
		oslDao.insert(comment);
	    return comment;
    }

	@Override
    public StoreCommentDetailsList listStoreComments(String storeId) {
		List<StoreComment> comments = oslDao.query(StoreComment.class, Collections.singletonMap("storeId", storeId));
		
		List<User> users = favouriteDao.listLikedUsersForStore(storeId);
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
		}
		detailsList.setUser(user);
		
	    return detailsList;
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void submitOffer(Offer offer) {
		oslDao.insert(offer);
    }

}
