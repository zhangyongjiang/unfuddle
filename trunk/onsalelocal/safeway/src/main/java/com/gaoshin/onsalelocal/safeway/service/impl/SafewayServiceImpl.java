package com.gaoshin.onsalelocal.safeway.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.safeway.dao.SafewayDao;
import com.gaoshin.onsalelocal.safeway.service.SafewayService;
import common.db.dao.GeoDao;
import common.geo.yahoo.Yapi;
import common.geo.yahoo.Yresponse;

@Service("safewayService")
@Transactional(readOnly=true)
public class SafewayServiceImpl extends ServiceBaseImpl implements SafewayService {
	@Autowired private SafewayDao safewayDao;
	@Autowired private GeoDao geoDao;

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void geo() {
		List<Store> stores = safewayDao.getNoGeoStores();
		for(Store store : stores) {
			String location = store.getAddress();
			try {
				Yresponse resp = Yapi.geo(location);
				store.setLatitude(new BigDecimal(resp.getResult().getLatitude()));
				store.setLongitude(new BigDecimal(resp.getResult().getLongitude()));
				store.setAddress(resp.getResult().getLine1());
				store.setCity(resp.getResult().getCity());
				store.setState(resp.getResult().getStatecode());
				store.setZipcode(resp.getResult().getUzip());
			} catch (Exception e) {
				e.printStackTrace();
				store.setLatitude(new BigDecimal(0));
				store.setLongitude(new BigDecimal(0));
			}
			safewayDao.updateEntity(store, "latitude", "longitude", "address", "city", "state", "zipcode");
		}
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void updateTasks() {
		safewayDao.updateTasks();
    }

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void seedTasks() {
		safewayDao.seedTasks();
    }

}
