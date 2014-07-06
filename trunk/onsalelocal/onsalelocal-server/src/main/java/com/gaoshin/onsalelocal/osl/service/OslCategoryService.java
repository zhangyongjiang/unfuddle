package com.gaoshin.onsalelocal.osl.service;

import com.nextshopper.api.Category;
import com.nextshopper.api.CategoryBean;

public interface OslCategoryService {
	CategoryBean getTopCategories();

	void create(Category cat);
}
