package com.gaoshin.onsalelocal.api.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.api.DataSource;
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
	    String sql = "select count(*) from CompanyDeal where merchantId=:storeId";
		Map<String, String> paramMap = Collections.singletonMap("storeId", storeId);
		return getNamedParameterJdbcTemplate().queryForInt(sql, paramMap);
    }

	@Override
    public int getFollowCount(String storeId) {
	    String sql = "select count(*) from FavouriteStore where storeId=:storeId";
		Map<String, String> paramMap = Collections.singletonMap("storeId", storeId);
		return getNamedParameterJdbcTemplate().queryForInt(sql, paramMap);
    }

	@Override
    public void updateStoreImageUrl(String storeId, String imgUrl) {
		String sql = "update Store set logo=:logo where id=:id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", storeId);
		params.put("logo", imgUrl);
		getNamedParameterJdbcTemplate().update(sql, params);
    }

	@Override
    public Store getStoreBySourceId(DataSource source, String sourceId) {
		String sql = "select s.* from StoreSource ss, Store s where ss.source=? and ss.sourceId=? and ss.storeId=s.id";
		Store db = queryUniqueBySql(Store.class, new Object[]{source.name(), sourceId}, sql);
	    return db;
    }

	@Override
    public List<Store> getChainStores(String merchantId) {
		String sql = "select s.* from Store s, ParentStore p1, ParentStore p2 where s.id=p1.storeId and p1.id=p2.id and p2.storeId=?";
	    return queryBySql(Store.class, sql, merchantId);
    }

}
