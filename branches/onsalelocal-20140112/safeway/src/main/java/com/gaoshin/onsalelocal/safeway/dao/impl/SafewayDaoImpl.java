package com.gaoshin.onsalelocal.safeway.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.safeway.dao.SafewayDao;
import com.gaoshin.onsalelocal.safeway.service.impl.SafewayStoreTaskHandler;
import common.db.dao.City;
import common.db.dao.impl.GenericDaoImpl;
import common.geo.GeoRange;
import common.geo.Geocode;

@Repository("safewayDao")
public class SafewayDaoImpl extends GenericDaoImpl implements SafewayDao {
	private static final Logger log = Logger.getLogger(SafewayDaoImpl.class);

	@Override
	public void updateTasks() {
		long now = System.currentTimeMillis();
		long before = now - 24 * 3600000;
		String sql = "update safeway.Task set status='Ready', updated=? where type=? and updated<?";
		int updated = super.update(sql, now, SafewayStoreTaskHandler.SafewayStoreTask, before);
		log.info("Updated " + updated + " tasks to Ready status");
	}

	@Override
	public List<Offer> listOffersIn(float lat, float lng, int radius, int offset, int size) {
		GeoRange range = Geocode.getRange(lat, lng, radius);
		String sql = "select * from Offer where latitude>:minLat and latitude < :maxLat and longitude>:minLng and longitude<:maxLng order by id limit :offset, :size";
		Map params = new HashMap();
		params.put("minLat", range.getMinLat());
		params.put("maxLat", range.getMaxLat());
		params.put("minLng", range.getMinLng());
		params.put("maxLng", range.getMaxLng());
		params.put("offset", offset);
		params.put("size", size);
		return queryBySql(Offer.class, params, sql);
	}

	@Override
	public List<Offer> listOffersIn(List<City> nearbyCities, int offset,
			int size) {
		List<String> cities = new ArrayList<String>();
		for(City city : nearbyCities) {
			cities.add(city.getCity());
		}
		String sql = "select * from Offer where city in (:cities) order by id limit :offset, :size";
		Map params = new HashMap();
		params.put("cities", cities);
		params.put("offset", offset);
		params.put("size", size);
		return queryBySql(Offer.class, params, sql);
	}

	@Override
	public List<Store> getNoGeoStores() {
		String sql = "select * from Store where latitude is null";
		return queryBySql(Store.class, null, sql);
	}

	@Override
    public void seedTasks() {
		long now = System.currentTimeMillis();
		String sql = "insert into Task (id, type, param, created, updated, status, tried, succeed)" +
					" select uuid(), '" + SafewayStoreTaskHandler.SafewayStoreTask + "'" +
					", id" +
					", " + now +
					", " + now + 
					", 'Created', 0, 0" + 
					" from Store";
		update(sql);
    }

}
