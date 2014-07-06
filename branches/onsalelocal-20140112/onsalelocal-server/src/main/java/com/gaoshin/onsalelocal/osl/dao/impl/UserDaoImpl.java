package com.gaoshin.onsalelocal.osl.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.api.DataSource;
import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.osl.dao.UserDao;
import com.gaoshin.onsalelocal.osl.entity.Account;
import com.gaoshin.onsalelocal.osl.entity.AccountType;
import com.gaoshin.onsalelocal.osl.entity.NotificationStatus;
import com.gaoshin.onsalelocal.osl.entity.User;
import com.gaoshin.onsalelocal.osl.entity.UserDetails;
import common.db.dao.ConfigDao;
import common.db.dao.impl.GenericDaoImpl;
import common.util.reflection.ReflectionUtil;

@Repository("userDao")
public class UserDaoImpl extends GenericDaoImpl implements UserDao {
	private static final Logger log = Logger.getLogger(UserDaoImpl.class);
	
	@Autowired private ConfigDao configDao;

	@Override
	public User getUserByLogin(String login) {
		return getUniqueResult(User.class, Collections.singletonMap("login", login));
	}

	@Override
    public Account getAccount(String userId, AccountType type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("type", type.name());
		return getUniqueResult(Account.class, params);
    }

	@Override
    public int getFollowingCount(String userId) {
	    String sql = "select count(*) from Follow where followerId=:followerId";
		Map<String, String> params = Collections.singletonMap("followerId", userId);
		return getNamedParameterJdbcTemplate().queryForInt(sql, params);
    }

	@Override
    public int getFollowerCount(String userId) {
	    String sql = "select count(*) from Follow where userId=:userId";
		Map<String, String> params = Collections.singletonMap("userId", userId);
		return getNamedParameterJdbcTemplate().queryForInt(sql, params);
    }

	@Override
    public int getOffersSubmitted(String userId) {
	    String sql = "select count(*) from Offer where source='" + DataSource.User + "' and sourceId=:userId";
		Map<String, String> params = Collections.singletonMap("userId", userId);
		return getNamedParameterJdbcTemplate().queryForInt(sql, params);
    }

	@Override
    public int getOffersLiked(String userId) {
	    String sql = "select count(*) from FavouriteOffer where userId=:userId";
		Map<String, String> params = Collections.singletonMap("userId", userId);
		return getNamedParameterJdbcTemplate().queryForInt(sql, params);
    }

	@Override
    public int getStoresFollowing(String userId) {
	    String sql = "select count(*) from FavouriteStore where userId=:userId";
		Map<String, String> params = Collections.singletonMap("userId", userId);
		return getNamedParameterJdbcTemplate().queryForInt(sql, params);
    }

	@Override
    public List<Offer> listOffersCreatedByUser(String userId, int offset, int size) {
	    String sql = "select * from Offer where source='" + DataSource.User + "' and sourceId=? order by updated desc limit ?,?";
		return queryBySql(Offer.class, sql, userId, offset, size);
    }

	@Override
    public List<Store> listStoresCreatedByUser(String userId) {
	    String sql = "select * from Store where ownerUserId=:userId";
		Map<String, String> params = Collections.singletonMap("userId", userId);
		return queryBySql(Store.class, params, sql);
    }

	@Override
    public void enableNotification(String userId, boolean enable) {
		String sql = "update User set noti=? where id=?";
		update(sql, enable?NotificationStatus.Enable.name() : NotificationStatus.Disable.name(), userId);
    }

	@Override
    public List<User> listLatestUsers(int offset, int size) {
	    return queryBySql(User.class, "select * from User order by updated desc limit ?, ?", offset, size);
    }

	@Override
    public UserDetails getUserDetails(String userId) {
		User u = getEntity(User.class, userId);
		UserDetails d = ReflectionUtil.copy(UserDetails.class, u);
		logger.info("getFollowingCount");
		int followings = getFollowingCount(userId);
		logger.info("getFollowerCount");
		int followers = getFollowerCount(userId);
		logger.info("getOffersSubmitted");
		int offers = getOffersSubmitted(userId);
		logger.info("getOffersLiked");
		int likes = getOffersLiked(userId);
		logger.info("getSToresFollowing");
		int stores = getStoresFollowing(userId);
		logger.info("getUnreadNotificationCount");
		int notis = getUnreadNotificationCount(userId);
		d.setFollowers(followers);
		d.setFollowings(followings);
		d.setOffers(offers);
		d.setLikes(likes);
		d.setStores(stores);
		d.setNotifications(notis);

		Account fbacc = getAccount(d.getId(), AccountType.Facebook);
		if(fbacc != null)
			d.setFbid(fbacc.getExtId());
		
		return d;
    }

	private int getUnreadNotificationCount(String userId) {
	    return getJdbcTemplate().queryForInt("select count(*) from Notification where unread=1 and userId=?", userId); 
    }

	@Override
    public List<User> getUserList(List<String> userIds) {
		if(userIds == null || userIds.size() == 0)
			return new ArrayList<User>();
	    return queryBySql(User.class, Collections.singletonMap("userIds", userIds), "select * from User where id in (:userIds)");
    }

	@Override
    public Map<String, User> getUserMap(List<String> userIds) {
		Map<String, User> map = new HashMap<String, User>();
		for(User u : getUserList(userIds)) {
			map.put(u.getId(), u);
		}
	    return map;
    }
}
