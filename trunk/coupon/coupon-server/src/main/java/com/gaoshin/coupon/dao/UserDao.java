package com.gaoshin.coupon.dao;

import java.util.List;

import com.gaoshin.coupon.entity.Store;
import com.gaoshin.coupon.entity.User;

public interface UserDao extends GenericDao {
    List<String> listUserTopStoreIds(String userId);

    boolean isOwner(String userId, String storeId);

    List<Store> listUserTopStores(String userId);

    User getUserByLogin(String login);
}
