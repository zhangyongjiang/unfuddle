package com.gaoshin.onsalelocal.osl.service;

import com.gaoshin.onsalelocal.api.Category;
import com.gaoshin.onsalelocal.api.CategoryBean;

public interface OslCategoryService {
	CategoryBean getTopCategories();

	void create(Category cat);
}
