package com.gaoshin.onsalelocal.safeway.dao;

import java.util.List;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.Store;
import common.db.dao.City;
import common.db.dao.GenericDao;

public interface SafewayDao extends GenericDao {

	void updateTasks();

	List<Offer> listOffersIn(float lat, float lng, int radius, int offset, int size);

	List<Offer> listOffersIn(List<City> nearbyCities, int offset, int size);

	List<Store> getNoGeoStores();

	void seedTasks();

}
