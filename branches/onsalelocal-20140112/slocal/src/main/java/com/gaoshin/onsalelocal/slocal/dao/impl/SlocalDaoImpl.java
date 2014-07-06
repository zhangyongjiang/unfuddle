package com.gaoshin.onsalelocal.slocal.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.slocal.dao.SlocalDao;
import com.gaoshin.onsalelocal.slocal.service.impl.SlocalCityCategoryTaskHandler;
import com.gaoshin.onsalelocal.slocal.service.impl.SlocalCityCategoryTopDealsHandler;
import com.gaoshin.onsalelocal.slocal.service.impl.SlocalCityTaskHandler;
import common.db.dao.City;
import common.db.dao.impl.GenericDaoImpl;
import common.db.entity.Task;
import common.db.task.TaskStatus;
import common.geo.GeoRange;
import common.geo.Geocode;

@Repository("slocalDao")
public class SlocalDaoImpl extends GenericDaoImpl implements SlocalDao {
	private static final Logger log = Logger.getLogger(SlocalDaoImpl.class);

	@Override
	public void updateTasks() {
		String sql = "select count(*) from slocal.Task where type=? and status=?";
		int cnt = getJdbcTemplate().queryForInt(sql, SlocalCityTaskHandler.CityTask, TaskStatus.Ready.name());
		if(cnt > 0)
			return;
		
		long now = System.currentTimeMillis();
		long before = now - 24 * 3600000;
		sql = "update slocal.Task set status='Ready', updated=? where (status = 'Succeed' or status='Created') and type=? and updated<? order by updated limit 10";
		int updated = super.update(sql, now, SlocalCityTaskHandler.CityTask, before);
		log.info("Updated " + updated + " tasks to Ready status");
	}

	@Override
	public void seedCityTasks() {
		String sql = "insert into Task (id, type, param, created, updated, status, tried, succeed)" +
					" select uuid(), '" + SlocalCityTaskHandler.CityTask + "'" +
					", city.id" +
					", " + 0 +
					", " + 0 + 
					", 'Created', 0, 0" + 
					" from geo.uscities city where city.population is not null";
		update(sql);
	}

	@Override
	public void seedCityCategoryTasks(String cityId) {
		long now = System.currentTimeMillis();
		String delsql = "delete from slocal.Task where param=? and type=?";
		update(delsql, cityId, SlocalCityCategoryTaskHandler.CityCategoryTask);
		String sql = "insert into slocal.Task (id, type, param, param1, created, updated, status, tried, succeed)" +
					" select uuid(), '" + SlocalCityCategoryTaskHandler.CityCategoryTask + "'" +
					", '" + cityId + "'" +  
					", cat.id" +
					", " + now +
					", " + now + 
					", 'Ready', 0, 0" + 
					" from slocal.Category cat where cat.parentId is not null and cat.priority=10";
		update(sql);
	}

	@Override
	public void seedCityCategoryTopDeals(String cityId) {
		long now = System.currentTimeMillis();
		String delsql = "delete from slocal.Task where param=? and type=?";
		update(delsql, cityId, SlocalCityCategoryTopDealsHandler.CityCategoryTopDealsTask);
		String sql = "insert into slocal.Task (id, type, param, param1, created, updated, status, tried, succeed)" +
					" select uuid(), '" + SlocalCityCategoryTopDealsHandler.CityCategoryTopDealsTask + "'" +
					", '" + cityId + "'" +  
					", cat.id" +
					", " + now +
					", " + now + 
					", 'Ready', 0, 0" + 
					" from slocal.Category cat where cat.parentId is null and cat.priority=10";
		update(sql);
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
	public int getNumberOfCityTasks() {
		String sql = "select count(*) from slocal.Task where type=?";
		return getJdbcTemplate().queryForInt(sql, SlocalCityTaskHandler.CityTask);
	}

	@Override
	public int getPendingCityCategoryTaskCount() {
		String sql = "select count(*) from Task where type=? and status=?";
		int cnt = getJdbcTemplate().queryForInt(sql, SlocalCityCategoryTaskHandler.CityCategoryTask, TaskStatus.Ready.name());
		return cnt;
	}

	@Override
	public void ondemand(String cityId) {
		long onedayago = System.currentTimeMillis() - 24*3600000;
		String sql = "select * from slocal.Task where type='SlocalCityTask' and param=:param";
		Task task = queryUniqueBySql(Task.class, Collections.singletonMap("param", cityId), sql);
		if(task == null || (task.getUpdated() < onedayago && task.getUpdated()!=0))
			return;
		
		log.info("ondemandShoplocal request for city " + cityId);
		sql = "update slocal.Task set updated=?, status='Succeed' where id=?";
		update(sql, System.currentTimeMillis(), task.getId());
		
		String delsql = "delete from slocal.Task where param=? and type=?";
		update(delsql, cityId, "CityCategoryTask");
		sql = "insert into slocal.Task (id, type, param, param1, created, updated, status, tried, succeed)" +
					" select uuid(), 'CityCategoryTask'" +
					", '" + cityId + "'" +  
					", cat.id" +
					", 0" +
					", 0" + 
					", 'Ready', 0, 0" + 
					" from slocal.Category cat";
		update(sql);
	}
}
