package com.gaoshin.onsalelocal.slocal.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.OfferDetails;
import com.gaoshin.onsalelocal.api.OfferDetailsList;
import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.slocal.dao.SlocalDao;
import com.gaoshin.onsalelocal.slocal.service.SlocalService;
import common.db.dao.GeoDao;
import common.geo.GeoService;
import common.geo.Geocode;
import common.geo.Location;
import common.geo.google.GoogleGeoService;
import common.util.reflection.ReflectionUtil;

@Service("slocalService")
@Transactional(readOnly=true)
public class SlocalServiceImpl extends ServiceBaseImpl implements SlocalService {
	@Autowired private SlocalDao slocalDao;
	@Autowired private GeoDao geoDao;
	private GeoService geoService = new GoogleGeoService();

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void updateTasks() {
		slocalDao.updateTasks();
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void seedCityTasks() {
		int cnt = slocalDao.getNumberOfCityTasks();
		if(cnt == 0) {
			slocalDao.seedCityTasks();
		}
	}

	@Override
	public OfferDetailsList searchOffer(final float lat, final float lng, int radius, int offset, int size) {
		OfferDetailsList list = new OfferDetailsList();
		List<Offer> offers = slocalDao.listOffersIn(lat, lng, radius, offset, size);
		for(Offer o : offers) {
			OfferDetails details = ReflectionUtil.copy(OfferDetails.class, o);
			details.setRootSource("Shoplocal");
			list.getItems().add(details);
			float dis1 = Geocode.distance(o.getLatitude(), o.getLongitude(), lat, lng);
			details.setDistance(dis1);
		}
		Collections.sort(list.getItems(), new Comparator<OfferDetails>() {
			@Override
			public int compare(OfferDetails o1, OfferDetails o2) {
				return (int) ((o1.getDistance() - o2.getDistance()) * 1000);
			}
		});
		return list;
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void geo() {
		List<Store> stores = slocalDao.getNoGeoStores();
		for(Store store : stores) {
			String location = store.getAddress() + ", " + store.getCity() + ", " + store.getState();
			try {
				Location loc = geoService.geo(location);
				store.setLatitude(new BigDecimal(loc.getGeocode().getLatitude()));
				store.setLongitude(new BigDecimal(loc.getGeocode().getLongitude()));
			} catch (Exception e) {
				e.printStackTrace();
				store.setLatitude(new BigDecimal(0));
				store.setLongitude(new BigDecimal(0));
			}
			slocalDao.updateEntity(store, "latitude", "longitude");
		}
	}

	@Override
	public int getPendingCityCategoryTaskCount() {
		return slocalDao.getPendingCityCategoryTaskCount();
	}

	@Override
	public void ondemand(String cityId) {
		slocalDao.ondemand(cityId);
	}
}
