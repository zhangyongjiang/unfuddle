package com.gaoshin.onsalelocal.osl.dao.impl;

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
import com.gaoshin.onsalelocal.osl.entity.User;
import common.db.dao.ConfigDao;
import common.db.dao.impl.GenericDaoImpl;

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
    public List<Offer> listOffersCreatedByUser(String userId) {
	    String sql = "select * from Offer where source='" + DataSource.User + "' and sourceId=:userId";
		Map<String, String> params = Collections.singletonMap("userId", userId);
		return queryBySql(Offer.class, params, sql);
    }

	@Override
    public List<Store> listStoresCreatedByUser(String userId) {
	    String sql = "select * from Store where ownerUserId=:userId";
		Map<String, String> params = Collections.singletonMap("userId", userId);
		return queryBySql(Store.class, params, sql);
    }
}
