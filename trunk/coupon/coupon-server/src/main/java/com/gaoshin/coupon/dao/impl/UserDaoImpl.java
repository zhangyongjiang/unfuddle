package com.gaoshin.coupon.dao.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gaoshin.coupon.bean.StoreTree;
import com.gaoshin.coupon.dao.StoreDao;
import com.gaoshin.coupon.dao.UserDao;
import com.gaoshin.coupon.entity.Store;
import com.gaoshin.coupon.entity.User;
import com.gaoshin.coupon.entity.UserStoreAccount;

@Repository
public class UserDaoImpl extends GenericDaoImpl implements UserDao {
    @Autowired StoreDao storeDao;
    
    @Override
    public List<String> listUserTopStoreIds(String userId) {
        return queryColumn(UserStoreAccount.class, Collections.singletonMap("userId", userId), "storeId");
    }
    
    @Override
    public List<Store> listUserTopStores(String userId) {
        String sql = "select * from Store where id in (select storeId from UserStoreAccount where userId=:userId)";
        return queryBySql(Store.class, Collections.singletonMap("userId", userId), sql);
    }
    
    @Override
    public boolean isOwner(String userId, String checkStoreId) {
        List<String> userTopStoreIds = listUserTopStoreIds(userId);
        boolean isOwner = false;
        for(String storeId : userTopStoreIds) {
            isOwner = checkStoreId.equals(storeId);
            if(isOwner) {
                break;
            }
            
            StoreTree tree = storeDao.getBranchTree(storeId, "id", "parentId");
            isOwner = tree.hasChild(checkStoreId);
            if(isOwner) {
                break;
            }
        }
        return isOwner;
    }

    @Override
    public User getUserByLogin(String login) {
        return getUniqueResult(User.class, Collections.singletonMap("login", login), "*");
    }
}
