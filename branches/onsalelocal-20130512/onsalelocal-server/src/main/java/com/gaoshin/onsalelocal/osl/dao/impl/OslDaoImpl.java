package com.gaoshin.onsalelocal.osl.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.api.StoreList;
import com.gaoshin.onsalelocal.osl.dao.OslDao;
import common.db.dao.City;
import common.db.dao.ConfigDao;
import common.db.dao.impl.GenericDaoImpl;
import common.db.entity.Task;
import common.db.util.ORMClass;
import common.geo.GeoRange;
import common.geo.Geocode;

@Repository("oslDao")
public class OslDaoImpl extends GenericDaoImpl implements OslDao {
	private static final Logger log = Logger.getLogger(OslDaoImpl.class);
	
	private static final String LastHarvestTime = "LastHavestTime";
	
	@Autowired private ConfigDao configDao;

	@Override
	public List<Offer> listOffersIn(float lat, float lng, int radius, int offset, int size, String source) {
		GeoRange range = Geocode.getRange(lat, lng, radius);
		Map params = new HashMap();
		String sourceClause = ((source == null) ? "" : (" source=:source and "));
		String sql = "select * from osl.Offer where " + sourceClause + " latitude>:minLat and latitude < :maxLat and longitude>:minLng and longitude<:maxLng order by id limit :offset, :size";
		params.put("minLat", range.getMinLat());
		params.put("maxLat", range.getMaxLat());
		params.put("minLng", range.getMinLng());
		params.put("maxLng", range.getMaxLng());
		params.put("offset", offset);
		params.put("size", size);
		params.put("source", source);
		return queryBySql(Offer.class, params, sql);
	}

	@Override
	public int harvest(String schema) {
		String name = LastHarvestTime + "." + schema;
		long t = configDao.getLong(name, 0l);
		ORMClass oc = getOrmClass(Offer.class);
		String sql = "insert ignore into osl.Offer (" + oc.getColumns() + ") select " + oc.getColumns() + " from " + schema + ".Offer where updated > " + t;
		int update = update(sql);
		log.info("harvest " + update + " offers from " + schema);
		configDao.set(name, System.currentTimeMillis());
		return update;
	}

	@Override
	public void cleanup(int days) {
		long day7 = System.currentTimeMillis() - days * 24 * 3600000;
		String sql = "delete from osl.Offer where updated<" + day7;
		int update = update(sql);
		log.info("deleted offers which are " + days + " days old: " + update);
	}

	@Override
	public void ondemandShoplocal(City city) {
		long onedayago = System.currentTimeMillis() - 24*3600000;
		String sql = "select * from slocal.Task where type='SlocalCityTask' and param=:param";
		Task task = queryUniqueBySql(Task.class, Collections.singletonMap("param", city.getId()), sql);
		if(task == null || (task.getUpdated() > onedayago && task.getUpdated()!=0))
			return;
		
		log.info("ondemandShoplocal request for city " + city.getId() + " " + city.getCity() + ", " + city.getState());
		sql = "update slocal.Task set updated=?, status='Succeed' where id=?";
		update(sql, System.currentTimeMillis(), task.getId());
		
		String delsql = "delete from slocal.Task where param=? and type=?";
		update(delsql, city.getId(), "CityCategoryTask");
		sql = "insert into slocal.Task (id, type, param, param1, created, updated, status, tried, succeed)" +
					" select uuid(), 'CityCategoryTask'" +
					", '" + city.getId() + "'" +  
					", cat.id" +
					", 0" +
					", 0" + 
					", 'Ready', 0, 0" + 
					" from slocal.Category cat";
		update(sql);
		
	}

	@Override
    public StoreList nearbyStores(Float latitude, Float longitude, Float radius, String category, String subcategory, Boolean serviceOnly) {
		StoreList list = new StoreList();
		GeoRange range = Geocode.getRange(latitude, longitude, radius);
		
		RowMapper<Store> rowMapper = new RowMapper<Store>(){
			@Override
            public Store mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
				Store store = new Store();
				store.setName(rs.getString("merchant"));
				store.setPhone(rs.getString("phone"));
				store.setAddress(rs.getString("address"));
				store.setCity(rs.getString("city"));
				store.setState(rs.getString("state"));
				store.setCreated(rs.getLong("cnt"));
				return store;
            }};
		
		String sql = "select merchant, phone, address, city, state, count(*) as cnt from osl.Offer where " +
				"latitude>" + range.getMinLat() +
				" and latitude < " + range.getMaxLat() +
				" and longitude>" + range.getMinLng() +
				" and longitude<" + range.getMaxLng() +
				" __category__  __subcategory__ " +
				" __service__ " +
				" and ( 3959 * acos( cos( radians(" + latitude + ") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(" + longitude + ") ) + sin( radians(" + latitude + ") ) * sin( radians( latitude ) ) ) ) < " + radius + 
				" group by merchant";
		Map<String, Object> params = new HashMap<String, Object>();
		if(category == null) {
			sql = sql.replaceAll("__category__", "");
		}
		else {
			sql = sql.replaceAll("__category__", " and category=:category");
			params.put("category", category);
		}
		if(subcategory == null) {
			sql = sql.replaceAll("__subcategory__", "");
		}
		else {
			sql = sql.replaceAll("__subcategory__", " and subcategory=:subcategory");
			params.put("subcategory", subcategory);
		}
		if(serviceOnly!=null) {
			if(serviceOnly) {
				sql = sql.replaceAll("__service__", " and source='yipit' ");
			}
			else {
				sql = sql.replaceAll("__service__", " and source!='yipit' ");
			}
		}
		else {
			sql = sql.replaceAll("__service__", "");
		}
		
		logger.info("nearby stores " + sql);
		list.getItems().addAll(getNamedParameterJdbcTemplate().query(sql, params, rowMapper));
		
	    return list;
    }

	@Override
    public List<Offer> listStoreOffers(String storeId) {
	    return query(Offer.class, Collections.singletonMap("storeId", storeId));
    }

}
