package com.gaoshin.coupon.dao;

import java.util.List;

import com.gaoshin.coupon.bean.StoreTree;
import com.gaoshin.coupon.entity.Store;

public interface StoreDao extends GenericDao {
    Store getStore(String id, String... columns);
    Store getParent(String storeId, String... columns);
    List<Store> getBranches(String storeId, String... columns);
    StoreTree getParentTree(String storeId, String... columns);
    StoreTree getBranchTree(String storeId, String... columns);
    StoreTree getBranchTree(Store store, String... columns);
    List<Store> listTopStores(String userId);
    List<Store> searchStoreByZipcode(List<String> zipcodes);
    List<Store> nearbyStores(Float lat, Float lng, float radius);
    List<Store> nearbyCharinStores(Float lat, Float lng, float radius, String[] strings);
    Store searchStore(String address, String city, String state, String merchantName);
    Store getOneStoreWithoutGeo();
}
