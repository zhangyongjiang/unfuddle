package com.gaoshin.onsalelocal.api.dao;

import java.util.List;
import java.util.Map;

import com.gaoshin.onsalelocal.api.CategoryMap;
import common.db.dao.GenericDao;

public interface CategoryDao extends GenericDao {

	List<CategoryMap> list();
	
	Map<String, String> getMap(String source);

}
