package com.gaoshin.coupon.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.gaoshin.coupon.bean.StoreTree;
import com.gaoshin.coupon.dao.StoreDao;
import com.gaoshin.coupon.entity.Store;
import common.geo.GeoRange;
import common.geo.Geocode;
import common.util.db.SqlUtil;
import common.util.reflection.ReflectionUtil;

@Repository
public class StoreDaoImpl extends GenericDaoImpl implements StoreDao {
    @Override
    public Store getStore(String id, String... columns) {
        return getEntity(Store.class, id, columns);
    }

    public List<String> getAncestorIds(String storeId) {
        Store store = getEntity(Store.class, storeId, "parentId");
        return getAncestorIds(store);
    }

    public List<String> getAncestorIds(Store store) {
        List<String> ancestors = new ArrayList<String>();
        while(store.getParentId() != null) {
            ancestors.add(store.getParentId());
            store = getEntity(Store.class, store.getParentId(), "parentId");
        }
        return ancestors;
    }

    @Override
    public Store getParent(String storeId, String... columns) {
        String sql = "select " + SqlUtil.getStrColumns(columns) + " from Store where id in (select parentId from Store where id=:id)";
        Store store = queryUniqueBySql(Store.class, Collections.singletonMap("id", storeId), sql);
        return store;
    }

    @Override
    public List<Store> getBranches(String storeId, String... columns) {
        return query(Store.class, Collections.singletonMap("parentId", storeId), columns);
    }

    @Override
    public StoreTree getParentTree(String storeId, String... columns) {
        StoreTree tree = new StoreTree();
        tree.setId(storeId);
        
        Store parent = getParent(storeId);
        if(parent != null) {
            tree.setParent(getParentTree(parent.getId(), columns));
        }
        
        return tree;
    }

    @Override
    public StoreTree getBranchTree(String storeId, String... columns) {
        Store m = new Store();
        m.setId(storeId);
        return getBranchTree(m, columns);
    }
    
    @Override
    public StoreTree getBranchTree(Store store, String... columns) {
        StoreTree tree = ReflectionUtil.copy(StoreTree.class, store);
        
        List<Store> branches = getBranches(store.getId(), columns);
        for(Store branch : branches) {
            tree.getChildren().add(getBranchTree(branch));
        }
        
        return tree;
    }

    @Override
    public List<Store> listTopStores(String userId) {
        return query(Store.class, Collections.singletonMap("userId", userId));
    }

    @Override
    public List<Store> searchStoreByZipcode(List<String> zipcodes) {
        String sql = "select * from Store where zipcode in (:zipcodes)";
        return queryBySql(Store.class, Collections.singletonMap("zipcodes", zipcodes), sql );
    }

    @Override
    public List<Store> nearbyStores(Float lat, Float lng, float radius) {
        GeoRange range = Geocode.getRange(lat, lng, radius);
        String sql = "select * from Store where latitude>:lat0 and latitude<:lat1 and longitude>:lng0 and longitude<:lng1 order by ((ACOS(SIN(:lat * PI() / 180) * SIN(latitude * PI() / 180) + COS(:lat * PI() / 180) * COS(latitude * PI() / 180) * COS((:lng - longitude) * PI() / 180)) * 180 / PI()) * 60 * 1.1515)";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("lat0", range.getMinLat());
        params.put("lat1", range.getMaxLat());
        params.put("lng0", range.getMinLng());
        params.put("lng1", range.getMaxLng());
        List<Store> list = this.getNamedParameterJdbcTemplate().query(sql, params, new RowMapper<Store>() {
            @Override
            public Store mapRow(ResultSet rs, int rowNum) throws SQLException {
                Store store = new Store();
                store.setId(rs.getString("id"));
                store.setName(rs.getString("name"));
                store.setParentId(rs.getString("parentId"));
                store.setChainStoreId(rs.getString("chainStoreId"));
                return store;
            }
        });
        return list;
    }

    @Override
    public List<Store> nearbyCharinStores(Float lat, Float lng, float radius, String[] names) {
        List<String> nameList = new ArrayList<String>();
        for(String s : names) {
            nameList.add(s);
        }
        GeoRange range = Geocode.getRange(lat, lng, radius);
        String sql = "select * from Store where name in (:names) and latitude>:lat0 and latitude<:lat1 and longitude>:lng0 and longitude<:lng1 order by ((ACOS(SIN(:lat * PI() / 180) * SIN(latitude * PI() / 180) + COS(:lat * PI() / 180) * COS(latitude * PI() / 180) * COS((:lng - longitude) * PI() / 180)) * 180 / PI()) * 60 * 1.1515)";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("lat0", range.getMinLat());
        params.put("lat1", range.getMaxLat());
        params.put("lng0", range.getMinLng());
        params.put("lng1", range.getMaxLng());
        params.put("names", nameList);
        return queryBySql(Store.class, params, sql);
    }

    @Override
    public Store searchStore(String address, String city, String state, String merchantName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("address", address);
        params.put("city", city);
        params.put("state", state);
        params.put("name", merchantName);
        return getUniqueResult(Store.class, params);
    }

    @Override
    public Store getOneStoreWithoutGeo() {
        String uuid = UUID.randomUUID().toString();
        String sql = "update Store set latitude=0.1, address2='" + uuid + "' where latitude is null limit 1";
        getJdbcTemplate().execute(sql);
        Store history = getUniqueResult(Store.class, Collections.singletonMap("address2", uuid));
        return history;
    }
}
