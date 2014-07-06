package com.gaoshin.coupon.service;

import java.io.IOException;

import com.gaoshin.coupon.bean.CouponList;
import com.gaoshin.coupon.bean.StoreList;
import com.gaoshin.coupon.bean.StoreTree;
import com.gaoshin.coupon.entity.Store;

public interface StoreService {
    void addStore(String userId, Store store);
    Store getStore(String id);
    StoreList listUserStores(String userId);
    StoreTree getBranchTree(String id);
    StoreList getBranches(String id);
    CouponList listCoupons(String id);
    StoreList listTopStores(String userId);
    void createWalmartStores() throws IOException;
    void createTargetStores() throws IOException;
    void createCvsStores() throws IOException;
    void createSafewayStores() throws IOException;
    int geocodeOneStore();
    void createShoplocalStores() throws IOException;
}
