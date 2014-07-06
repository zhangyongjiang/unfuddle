package com.gaoshin.coupon.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gaoshin.coupon.bean.CategoryList;
import com.gaoshin.coupon.dao.CategoryDao;
import com.gaoshin.coupon.entity.Category;

@Repository
public class CategoryDaoImpl extends GenericDaoImpl implements CategoryDao {

    @Override
    public CategoryList listTopCategories() {
        CategoryList cl = new CategoryList();
        String sql = "select * from Category where parentId is null";
        List<Category> list = queryBySql(Category.class, null, sql);
        cl.setItems(list);
        return cl;
    }
}
