package com.gaoshin.coupon.dao;

import com.gaoshin.coupon.bean.CategoryList;


public interface CategoryDao extends GenericDao {
    CategoryList listTopCategories();
}
