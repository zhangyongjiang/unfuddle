package com.gaoshin.onsalelocal.osl.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.osl.beans.Category;
import com.gaoshin.onsalelocal.osl.dao.CategoryDao;
import common.db.dao.impl.GenericDaoImpl;
import common.geo.GeoRange;
import common.geo.Geocode;

@Repository("categoryDao")
public class CategoryDaoImpl extends GenericDaoImpl implements CategoryDao {
	private static final Logger log = Logger.getLogger(CategoryDaoImpl.class);
	private HashSet<String> allCategories = new HashSet<String>();
	
	public synchronized HashSet<String> getAllCategories() {
		if(allCategories.size() == 0) {
			String sql = "select distinct(category) from Offer";
			getJdbcTemplate().query(sql, new RowMapper<String>(){
				@Override
                public String mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
					String name = rs.getString(1);
					if(name == null || name.trim().length() == 0)
						name = "Others";
					allCategories.add(name);
	                return null;
                }});
		}
		return new HashSet<String>(allCategories);
	}

	@Override
	public Category listTree() {
		Category root = new Category();
		Map<String, Category> map = new HashMap<String, Category>();
		
		String sql = "select * from slocal.Category where parentId is null";
		List<Category> list = queryBySql(Category.class, Collections.EMPTY_MAP, sql);
		for(Category cat : list) {
			map.put(cat.getId(), cat);
			root.getItems().add(cat);
		}
		
		sql = "select * from slocal.Category where parentId is not null";
		list = queryBySql(Category.class, Collections.EMPTY_MAP, sql);
		for(Category cat : list) {
			Category parent = map.get(cat.getParentId());
			if(parent != null) {
				parent.getItems().add(cat);
			}
		}
		
		return root;
	}

	@Override
    public Category nearby(Float latitude, Float longitude, Float radius, String merchant, Boolean localService, String category) {
		GeoRange range = Geocode.getRange(latitude, longitude, radius);
		String sql = "select category, count(*), subCategory from osl.Offer where __MERCHANT__ __LOCALSERVICE__ __CATEGORY__ " +
				" latitude>" + range.getMinLat() +
				" and latitude < " + range.getMaxLat() +
				" and longitude>" + range.getMinLng() +
				" and longitude<" + range.getMaxLng() +
				" and ( 3959 * acos( cos( radians(" + latitude + ") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(" + longitude + ") ) + sin( radians(" + latitude + ") ) * sin( radians( latitude ) ) ) ) < " + radius + 
				" group by category, subCategory";
		Object[] args = null;
		if(merchant != null) {
			sql = sql.replaceAll("__MERCHANT__", " merchant = ? and ");
			args = new Object[] {merchant};
		}
		else {
			sql = sql.replaceAll("__MERCHANT__", "  ");
			args = new Object[0];
		}
		
		if(localService!=null) {
			if(localService) {
				sql = sql.replaceAll("__LOCALSERVICE__", " source='yipit' and ");
			}
			else {
				sql = sql.replaceAll("__LOCALSERVICE__", " source!='yipit' and ");
			}
		}
		else {
			sql = sql.replaceAll("__LOCALSERVICE__", "");
		}
		
		if(category != null) {
			sql = sql.replaceAll("__CATEGORY__", " category='" + category.replaceAll("'", "''") + "' and ");
		}
		else {
			sql = sql.replaceAll("__CATEGORY__", "");
		}
		
		logger.info("nearby categories " + sql);
		final HashMap<String, Category> map = new HashMap<String, Category>();
		final Category result = new Category();
		getJdbcTemplate().query(sql, args, new RowCallbackHandler() {
			@Override
            public void processRow(ResultSet rs) throws SQLException {
				String name = rs.getString(1);
				Category cat = map.get(name);
				if(cat == null) {
					cat = new Category();
					cat.setName(name);
					map.put(name, cat);
				}
				int offers = rs.getInt(2);
				cat.setOfferCount(cat.getOfferCount() + offers);
				result.setOfferCount(result.getOfferCount() + offers);
				
				Category sub = new Category();
				sub.setOfferCount(offers);
				String subcatName = rs.getString(3);
				sub.setName(subcatName);
				cat.getItems().add(sub);
            }
		});

		result.getItems().addAll(map.values());
		Collections.sort(result.getItems(), new Comparator<Category>() {
			@Override
            public int compare(Category arg0, Category arg1) {
	            return arg1.getOfferCount() - arg0.getOfferCount();
            }
		});
		
		for(Category cat : result.getItems()) {
			Collections.sort(cat.getItems(), new Comparator<Category>() {
				@Override
	            public int compare(Category arg0, Category arg1) {
		            return arg1.getOfferCount() - arg0.getOfferCount();
	            }
			});
		}
		
		return result;
    }

	@Override
    public Category getTopCategories() {
		Category root = new Category();
		for(String s : getAllCategories()) {
			Category c = new Category();
			c.setName(s);
			c.setId(s);
			root.getItems().add(c);
		}
		return root;
    }
	
}
