package com.gaoshin.onsalelocal.groupon.dao;

import java.util.List;

import com.gaoshin.onsalelocal.groupon.beans.Category;

public interface CategoryDao {

    List<Category> getOrderedSubcategories();

}
