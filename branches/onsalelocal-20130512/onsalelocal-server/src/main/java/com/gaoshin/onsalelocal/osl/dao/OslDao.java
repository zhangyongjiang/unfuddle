package com.gaoshin.onsalelocal.osl.dao;

import java.util.List;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.StoreList;
import common.db.dao.City;
import common.db.dao.GenericDao;

public interface OslDao extends GenericDao {

	List<Offer> listOffersIn(float lat, float lng, int radius, int offset, int size, String source);

	int harvest(String schema);

	void cleanup(int days);

	void ondemandShoplocal(City city);

	StoreList nearbyStores(Float latitude, Float longitude, Float radius, String category, String subcategory, Boolean serviceOnly);

	List<Offer> listStoreOffers(String storeId);

}
