package com.gaoshin.cj.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gaoshin.cj.beans.Category;
import com.gaoshin.cj.dao.CategoryDao;

@Repository("categoryDao")
public class CategoryDaoImpl extends GenericDaoImpl implements CategoryDao {

    @Override
    public List<Category> getOrderedSubcategories() {
        String sql = "select * from category where parent_id != 'ROOT' order by id";
        return queryBySql(Category.class, null, sql);
    }

}
