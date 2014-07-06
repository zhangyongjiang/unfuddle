package com.gaoshin.onsalelocal.walmart.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.walmart.dao.WalmartDao;
import com.gaoshin.onsalelocal.walmart.service.WalmartService;
import common.db.dao.GeoDao;
import common.geo.GeoService;
import common.geo.Location;
import common.geo.google.GoogleGeoService;

@Service("walmartService")
@Transactional(readOnly=true)
public class WalmartServiceImpl extends ServiceBaseImpl implements WalmartService {
	@Autowired private WalmartDao walmartDao;
	@Autowired private GeoDao geoDao;
	private GeoService geoService = new GoogleGeoService();

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void geo() {
		List<Store> stores = walmartDao.getNoGeoStores();
		for(Store store : stores) {
			String location = store.getAddress();
			try {
				Location loc = geoService.geo(location);
				store.setLatitude(new BigDecimal(loc.getGeocode().getLatitude()));
				store.setLongitude(new BigDecimal(loc.getGeocode().getLongitude()));
				store.setAddress(loc.getAddr().toString());
				store.setCity(loc.getAddr().getCity());
				store.setState(loc.getAddr().getState());
				store.setZipcode(loc.getAddr().getZipcode());
			} catch (Exception e) {
				e.printStackTrace();
				store.setLatitude(new BigDecimal(0));
				store.setLongitude(new BigDecimal(0));
			}
			walmartDao.updateEntity(store, "latitude", "longitude", "address", "city", "state", "zipcode");
		}
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void updateTasks() {
		walmartDao.updateTasks();
    }

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void seedTasks() {
		walmartDao.seedTasks();
    }

}
