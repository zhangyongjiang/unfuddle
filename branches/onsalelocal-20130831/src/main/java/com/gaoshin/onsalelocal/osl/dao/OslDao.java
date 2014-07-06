package com.gaoshin.onsalelocal.osl.dao;

import java.util.List;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.Sort;
import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.api.StoreList;
import com.gaoshin.onsalelocal.osl.entity.Tag;
import common.db.dao.City;
import common.db.dao.GenericDao;

public interface OslDao extends GenericDao {

	List<Offer> listOffersIn(float lat, float lng, int radius, int offset, int size, String source, Sort sort);

	int harvest(String schema);

	void cleanup(int days);

	void ondemandShoplocal(City city);

	StoreList nearbyStores(Float latitude, Float longitude, Float radius, boolean hasOffer);

	List<Offer> listStoreOffers(String storeId);

	List<Tag> listTags();

	List<Offer> trend(float lat, float lng, int radius, int offset, int size);

	Store searchStore(String source, String sourceId, Store store);

}
