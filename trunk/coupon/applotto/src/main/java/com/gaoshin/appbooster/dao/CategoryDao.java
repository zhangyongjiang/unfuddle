package com.gaoshin.appbooster.dao;

import com.gaoshin.appbooster.bean.CategoryList;


public interface CategoryDao extends GenericDao {
    CategoryList listTopCategories();
}
