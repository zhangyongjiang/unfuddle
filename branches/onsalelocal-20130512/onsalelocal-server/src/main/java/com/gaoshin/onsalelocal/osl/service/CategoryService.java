package com.gaoshin.onsalelocal.osl.service;

import com.gaoshin.onsalelocal.osl.beans.Category;

public interface CategoryService {
	Category listTree();

	Category nearby(Float latitude, Float longitude, Float radius, String merchant, Boolean localService, String category);

	Category getTopCategories();
}
