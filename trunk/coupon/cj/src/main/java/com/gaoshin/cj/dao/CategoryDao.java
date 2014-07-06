package com.gaoshin.cj.dao;

import java.util.List;

import com.gaoshin.cj.beans.Category;

public interface CategoryDao {

    List<Category> getOrderedSubcategories();

}
