package com.gaoshin.onsalelocal.slocal.dao;

import java.util.List;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.Store;
import common.db.dao.City;
import common.db.dao.GenericDao;

public interface SlocalDao extends GenericDao {

	void updateTasks();

	void seedCityTasks();

	List<Offer> listOffersIn(float lat, float lng, int radius, int offset, int size);

	List<Offer> listOffersIn(List<City> nearbyCities, int offset, int size);

	List<Store> getNoGeoStores();

	void seedCityCategoryTasks(String cityId);

	void seedCityCategoryTopDeals(String cityId);

	int getNumberOfCityTasks();

	int getPendingCityCategoryTaskCount();

	void ondemand(String cityId);

}
