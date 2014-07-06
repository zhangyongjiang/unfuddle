package com.gaoshin.onsalelocal.osl.dao;

import com.gaoshin.onsalelocal.osl.beans.Category;
import common.db.dao.GenericDao;

public interface CategoryDao extends GenericDao {
	Category listTree();

	Category nearby(Float latitude, Float longitude, Float radius, String merchant, Boolean localService, String category);

	Category getTopCategories();
}
