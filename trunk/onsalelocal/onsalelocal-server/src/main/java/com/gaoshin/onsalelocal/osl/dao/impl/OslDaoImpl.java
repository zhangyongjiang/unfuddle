package com.gaoshin.onsalelocal.osl.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.osl.dao.OslDao;
import com.nextshopper.api.DataSource;
import com.nextshopper.api.Offer;
import com.nextshopper.api.Sort;
import com.nextshopper.api.Store;
import com.nextshopper.api.StoreList;
import com.nextshopper.api.StoreSource;
import com.nextshopper.osl.entity.Notification;
import com.nextshopper.osl.entity.Tag;
import com.nextshopper.osl.entity.User;

import common.db.dao.City;
import common.db.dao.ConfigDao;
import common.db.dao.impl.GenericDaoImpl;
import common.db.entity.Task;
import common.db.util.ORMClass;
import common.geo.GeoRange;
import common.geo.Geocode;

@Repository("oslDao")
public class OslDaoImpl extends GenericDaoImpl implements OslDao {
	private static final Logger log = Logger.getLogger(OslDaoImpl.class);
	
	private static final String LastHarvestTime = "LastHavestTime";
	
	@Autowired private ConfigDao configDao;

	@Override
	public List<Offer> listOffersIn(float lat, float lng, int radius, int offset, int size, String source, Sort sort) {
		GeoRange range = Geocode.getRange(lat, lng, radius);
		Map params = new HashMap();
		String sourceClause = ((source == null) ? "" : (" source=:source and "));
		String sql = "select * from Offer where " + sourceClause + " latitude>:minLat and latitude < :maxLat and longitude>:minLng and longitude<:maxLng __order__ limit :offset, :size";
		params.put("minLat", range.getMinLat());
		params.put("maxLat", range.getMaxLat());
		params.put("minLng", range.getMinLng());
		params.put("maxLng", range.getMaxLng());
		params.put("offset", offset);
		params.put("size", size);
		params.put("source", source);
		
		sql = sql.replaceAll("__order__", "order by id");
		
		return queryBySql(Offer.class, params, sql);
	}

	@Override
	public List<Offer> trend(float lat, float lng, int radius, int offset, int size) {
		GeoRange range = Geocode.getRange(lat, lng, radius);
		Map params = new HashMap();
		String sql = "select o.* from Offer o, OfferSummary s " +
				" where o.latitude>:minLat and o.latitude < :maxLat " +
				" and o.longitude>:minLng and o.longitude<:maxLng " +
				" and o.id = s.id " +
				" order by s.popularity desc limit :offset, :size";
		params.put("minLat", range.getMinLat());
		params.put("maxLat", range.getMaxLat());
		params.put("minLng", range.getMinLng());
		params.put("maxLng", range.getMaxLng());
		params.put("offset", offset);
		params.put("size", size);
		
		return queryBySql(Offer.class, params, sql);
	}

	@Override
	public int harvest(String schema) {
		String name = LastHarvestTime + "." + schema;
		long t = configDao.getLong(name, 0l);
		ORMClass oc = getOrmClass(Offer.class);
		String sql = "insert ignore into Offer (" + oc.getColumns() + ") select " + oc.getColumns() + " from " + schema + ".Offer where updated > " + t;
		int update = update(sql);
		log.info("harvest " + update + " offers from " + schema);
		configDao.set(name, System.currentTimeMillis());
		return update;
	}

	@Override
	public void cleanup(int days) {
		long day7 = System.currentTimeMillis() - days * 24 * 3600000;
		String sql = "delete from Offer where updated<" + day7;
		int update = update(sql);
		log.info("deleted offers which are " + days + " days old: " + update);
	}

	@Override
	public void ondemandShoplocal(City city) {
		if(true)
			return;
		long onedayago = System.currentTimeMillis() - 24*3600000;
		String sql = "select * from slocal.Task where type='SlocalCityTask' and param=:param";
		Task task = queryUniqueBySql(Task.class, Collections.singletonMap("param", city.getId()), sql);
		if(task == null || (task.getUpdated() > onedayago && task.getUpdated()!=0))
			return;
		
		log.info("ondemandShoplocal request for city " + city.getId() + " " + city.getCity() + ", " + city.getState());
		sql = "update slocal.Task set updated=?, status='Succeed' where id=?";
		update(sql, System.currentTimeMillis(), task.getId());
		
		String delsql = "delete from slocal.Task where param=? and type=?";
		update(delsql, city.getId(), "CityCategoryTask");
		sql = "insert into slocal.Task (id, type, param, param1, created, updated, status, tried, succeed)" +
					" select uuid(), 'CityCategoryTask'" +
					", '" + city.getId() + "'" +  
					", cat.id" +
					", 0" +
					", 0" + 
					", 'Ready', 0, 0" + 
					" from slocal.Category cat";
		update(sql);
		
	}

	@Override
    public StoreList nearbyStores(Float latitude, Float longitude, Float radius, boolean hasOffer) {
		StoreList list = new StoreList();
		GeoRange range = Geocode.getRange(latitude, longitude, radius);
		
		String sql = "select * from Store where " +
				"latitude>" + range.getMinLat() +
				" and latitude < " + range.getMaxLat() +
				" and longitude>" + range.getMinLng() +
				" and longitude<" + range.getMaxLng() +
				(hasOffer ? " and offers > 0 " : "") + 
				" and ( 3959 * acos( cos( radians(" + latitude + ") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(" + longitude + ") ) + sin( radians(" + latitude + ") ) * sin( radians( latitude ) ) ) ) < " + radius
				+ " order by ( 3959 * acos( cos( radians(" + latitude + ") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(" + longitude + ") ) + sin( radians(" + latitude + ") ) * sin( radians( latitude ) ) ) )"
				;
		
		logger.info("nearby stores " + sql);
		list.getItems().addAll(queryBySql(Store.class, Collections.EMPTY_MAP, sql));
		
	    return list;
    }

	@Override
    public List<Offer> listStoreOffers(String storeId, int offset, int size) {
		String sql = "select * from CompanyDeal where merchantId=? limit ?, ?";
	    return queryBySql(Offer.class, sql, storeId, offset, size);
    }

	@Override
    public List<Tag> listTags() {
	    String sql = "select * from Tag order by popularity desc limit 50";
		return queryBySql(Tag.class, null, sql );
    }

	@Override
    public Store searchStore(String source, String sourceId, Store store) {
		if(source != null && sourceId != null) {
			String sql = "select s.* from StoreSource ss, Store s where ss.source=? and ss.sourceId=? and ss.storeId=s.id";
			Store db = queryUniqueBySql(Store.class, new Object[]{source, sourceId}, sql);
			if(db != null)
				return db;
		}
		
		Store db = null;
		StoreList storeList = nearbyStores(store.getLatitude().floatValue(), store.getLongitude().floatValue(), 0.001f, false);
		for(Store s : storeList.getItems()) {
			if(s.getName().equalsIgnoreCase(store.getName())) {
				db = s;
				break;
			}
		}
		
		if(db != null && source != null && sourceId != null && !DataSource.User.name().equals(source)) {
			StoreSource ss = new StoreSource();
			ss.setSource(DataSource.valueOf(source));
			ss.setSourceId(sourceId);
			ss.setStoreId(db.getId());
			insert(ss);
		}
		
	    return db;
    }

	@Override
    public List<Offer> followingsDeals(String userId, int offset, int size) {
		String sql = "select o.* from Offer o, FavouriteOffer fo, Follow f" +
				" where f.followerId=? and f.userId=fo.userId and fo.offerId=o.id" +
				" limit ?, ?";
		List<Offer> list = queryBySql(Offer.class, sql, userId, offset, size);
	    return list;
    }

	@Override
    public List<Notification> getLatestNotifications(String userId, int offset,
            int size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("offset", offset);
		params.put("size", size);
		List<Notification> list = queryBySql(Notification.class, params, "select * from Notification where userId=:userId order by created desc limit :offset, :size");
	    return list;
    }

	@Override
    public Map<String, Store> getStoreMap(List<String> storeIds) {
		Map<String, Store> map = new HashMap<String, Store>();
		for(Store u : getStoreList(storeIds)) {
			map.put(u.getId(), u);
		}
	    return map;
    }

	@Override
	public List<Store> getStoreList(List<String> storeIds) {
		if(storeIds == null || storeIds.size() == 0)
			return new ArrayList<Store>();
	    return queryBySql(Store.class, Collections.singletonMap("storeIds", storeIds), "select * from Store where id in (:storeIds)");
    }

	@Override
	public List<Offer> getOfferList(List<String> offerIds) {
		if(offerIds == null || offerIds.size() == 0)
			return new ArrayList<Offer>();
	    return queryBySql(Offer.class, Collections.singletonMap("offerIds", offerIds), "select * from Offer where id in (:offerIds)");
    }

	@Override
    public Map<String, Offer> getOfferMap(List<String> offerIds) {
		Map<String, Offer> map = new HashMap<String, Offer>();
		for(Offer u : getOfferList(offerIds)) {
			map.put(u.getId(), u);
		}
	    return map;
    }

	@Override
    public List<User> listFollowers(String userId) {
		String sql = "select u.* from User u, Follow f where f.followerId=u.id and f.userId=:userId";
		List<User> users = queryBySql(User.class, Collections.singletonMap("userId", userId), sql );
		return users;
    }

	@Override
    public List<User> followingUsers(String userId) {
		if(userId == null)
			return new ArrayList<User>();
		String sql = "select u.* from User u, Follow f where f.userId=u.id and f.followerId=:userId";
		List<User> users = queryBySql(User.class, Collections.singletonMap("userId", userId), sql );
		return users;
    }

	@Override
    public Map<String, User> followingUserMap(String userId) {
		List<User> users = followingUsers(userId);
		Map<String, User> userMap = new HashMap<String, User>();
		for(User u : users) {
			userMap.put(u.getId(), u);
		}
		return userMap;
    }

	@Override
    public void markAllNotificationsRead(String userId) {
		update("update Notification set unread=0 where userId=?", userId);
    }

	@Override
    public int getTotalNotifications(String userId) {
	    return getJdbcTemplate().queryForInt("select count(*) from Notification where userId=?", userId); 
    }

	@Override
    public int getTotalUnreadNotifications(String userId) {
	    return getJdbcTemplate().queryForInt("select count(*) from Notification where userId=? and unread=1", userId); 
    }

}
