package com.gaoshin.coupon.service;

import java.io.IOException;

import com.gaoshin.coupon.bean.CategoryList;

public interface CategoryService {
    void importShoplocalCategories() throws IOException;
    CategoryList listTopCategories();
}
