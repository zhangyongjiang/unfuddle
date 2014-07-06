package com.gaoshin.onsalelocal.api.dao;

import java.util.List;

import com.gaoshin.onsalelocal.api.Store;
import common.db.dao.GenericDao;

public interface StoreDao extends GenericDao {

	List<Store> getStoresByAddress(String address, String city, String state);

	int getOfferCount(String storeId);

	int getFollowCount(String storeId);

}
