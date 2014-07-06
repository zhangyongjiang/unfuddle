package com.gaoshin.appbooster.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gaoshin.appbooster.bean.CategoryList;
import com.gaoshin.appbooster.dao.CategoryDao;
import com.gaoshin.appbooster.entity.Category;

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
