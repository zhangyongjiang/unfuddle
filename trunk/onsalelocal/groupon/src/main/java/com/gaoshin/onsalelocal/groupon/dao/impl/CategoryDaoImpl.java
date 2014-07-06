package com.gaoshin.onsalelocal.groupon.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.groupon.beans.Category;
import com.gaoshin.onsalelocal.groupon.dao.CategoryDao;
import common.db.dao.impl.GenericDaoImpl;

@Repository("categoryDao")
public class CategoryDaoImpl extends GenericDaoImpl implements CategoryDao {

    @Override
    public List<Category> getOrderedSubcategories() {
        String sql = "select * from category where parent_id != 'ROOT' order by id";
        return queryBySql(Category.class, null, sql);
    }

}
