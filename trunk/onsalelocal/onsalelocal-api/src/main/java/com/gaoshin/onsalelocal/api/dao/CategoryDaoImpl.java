package com.gaoshin.onsalelocal.api.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.api.CategoryBean;
import com.gaoshin.onsalelocal.api.CategoryMap;
import common.db.dao.impl.GenericDaoImpl;

@Repository("categoryDao")
public class CategoryDaoImpl extends GenericDaoImpl implements CategoryDao {
	private HashSet<String> allCategories = new HashSet<String>();
	private HashMap<String, CategoryBean> topCategories = new HashMap<String, CategoryBean>();

	@PostConstruct
	public void init() {
	}
	
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
    public synchronized CategoryBean getTopCategories() {
		CategoryBean root = new CategoryBean();
		if(topCategories.size() > 0) {
			root.setItems(new ArrayList<CategoryBean>(topCategories.values()));
		}
		else {
			String sql = "select * from Category where parentId is null order by id";
			List<CategoryBean> cats = getJdbcTemplate().query(sql , new RowMapper<CategoryBean>(){
				@Override
	            public CategoryBean mapRow(ResultSet rs, int rowNum)
	                    throws SQLException {
					CategoryBean cat = new CategoryBean();
					cat.setId(rs.getString("id"));
					cat.setName(rs.getString("name"));
					cat.setOfferCount(rs.getInt("offerCount"));
					cat.setImage(rs.getString("image"));
					cat.setParentId(rs.getString("parentId"));
					topCategories.put(cat.getName(), cat);
		            return cat;
	            }});
			root.setItems(cats);
		}
		return root;
    }

	@Override
    public boolean isTopCategory(String cat) {
		if(topCategories.size() == 0) {
			getTopCategories();
		}
	    return topCategories.containsKey(cat);
    }
	
	@Override
    public List<CategoryMap> list() {
	    return query(CategoryMap.class, null);
    }

	@Override
    public Map<String, String> getMap(String src) {
		Map<String, String> result = new HashMap<String, String>();
		String sql = "select * from CategoryMap where source=:source";
		List<CategoryMap> list = queryBySql(CategoryMap.class, Collections.singletonMap("source", src), sql);
		for(CategoryMap cm : list) {
			result.put(cm.getSrcCat(), cm.getOslCat());
		}
	    return result;
    }

	@Override
    public String checkTags(String tags) {
		String[] split = tags.split(";");
		StringBuilder sb = new StringBuilder();
		getTopCategories();
		String top = null;
		for(String s : split) {
			if(topCategories.containsKey(s)) {
				top = s;
			}
			else {
				if(sb.length() > 0)
					sb.append(";");
				sb.append(s);
			}
		}
	    return sb.length() == 0 ? top : (top + ";" + sb.toString());
    }

}
