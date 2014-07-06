package com.gaoshin.onsalelocal.api.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.api.Store;
import common.db.dao.impl.GenericDaoImpl;

@Repository("storeDao")
public class StoreDaoImpl extends GenericDaoImpl implements StoreDao {

	@Override
	public List<Store> getStoresByAddress(String address, String city,
			String state) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("address", address);
		params.put("city", city);
		params.put("state", state);
		return query(Store.class, params);
	}

	@Override
    public int getOfferCount(String storeId) {
	    String sql = "select count(*) from Offer where merchantId=:storeId";
		Map<String, String> paramMap = Collections.singletonMap("merchantId", storeId);
		return getNamedParameterJdbcTemplate().queryForInt(sql, paramMap);
    }

	@Override
    public int getFollowCount(String storeId) {
	    String sql = "select count(*) from FavouriteStore where storeId=:storeId";
		Map<String, String> paramMap = Collections.singletonMap("storeId", storeId);
		return getNamedParameterJdbcTemplate().queryForInt(sql, paramMap);
    }

}
