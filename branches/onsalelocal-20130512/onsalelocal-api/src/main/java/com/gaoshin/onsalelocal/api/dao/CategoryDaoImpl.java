package com.gaoshin.onsalelocal.api.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.api.CategoryMap;
import common.db.dao.impl.GenericDaoImpl;

@Repository("categoryDao")
public class CategoryDaoImpl extends GenericDaoImpl implements CategoryDao {

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

}
