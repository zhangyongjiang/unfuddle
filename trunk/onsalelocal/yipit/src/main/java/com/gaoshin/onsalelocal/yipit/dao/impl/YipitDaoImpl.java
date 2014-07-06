package com.gaoshin.onsalelocal.yipit.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.yipit.api.Tag;
import com.gaoshin.onsalelocal.yipit.dao.YipitDao;
import com.gaoshin.onsalelocal.yipit.entity.YipitCategory;
import com.gaoshin.onsalelocal.yipit.service.impl.YipitCityTaskHandler;
import com.gaoshin.onsalelocal.yipit.service.impl.YipitDivisionTaskHandler;
import common.db.dao.impl.GenericDaoImpl;
import common.geo.GeoRange;
import common.geo.Geocode;

@Repository("yipitDao")
public class YipitDaoImpl extends GenericDaoImpl implements YipitDao {

	@Override
	public void updateTasks() {
		long now = System.currentTimeMillis();
		long onedaybefore = now - 24 * 3600000;
		String sql = "update Task set status='Ready', updated=" + now + " where status = 'Succeed' and updated < " + onedaybefore;
		super.update(sql);
	}

	@Override
	public void seedCityTasks() {
		long now = System.currentTimeMillis();
		String sql = "insert into Task (id, type, param, created, updated, status, tried, succeed)" +
					" select id, '" + YipitCityTaskHandler.TaskType + "'" +
					", concat(lat, ',', lon)" +
					", " + now +
					", " + now + 
					", 'Created', 0, 0" + 
					" from geo.uscities where population is not null";
		update(sql);
	}

	@Override
	public void seedDivisionTasks() {
		long now = System.currentTimeMillis();
		String sql = "insert into Task (id, type, param, created, updated, status, tried, succeed)" +
					" select uuid(), '" + YipitDivisionTaskHandler.TaskType + "'" +
					", id" +
					", " + now +
					", " + now + 
					", 'Created', 0, 0" + 
					" from DbDivision";
		update(sql);
	}

	@Override
	public List<Offer> listOffersIn(float lat, float lng, int radius) {
		GeoRange range = Geocode.getRange(lat, lng, radius);
		String sql = "select * from Offer where latitude>:minLat and latitude < :maxLat and longitude>:minLng and longitude<:maxLng";
		Map params = new HashMap();
		params.put("minLat", range.getMinLat());
		params.put("maxLat", range.getMaxLat());
		params.put("minLng", range.getMinLng());
		params.put("maxLng", range.getMaxLng());
		return queryBySql(Offer.class, params, sql);
	}

	@Override
    public void insertTags(List<Tag> tags) {
		for(Tag tag : tags) {
			YipitCategory yc = new YipitCategory();
			yc.setName(tag.getName());
			yc.setSlug(tag.getSlug());
			yc.setUrl(tag.getUrl());
			insert(yc, true);
		}
    }
}
