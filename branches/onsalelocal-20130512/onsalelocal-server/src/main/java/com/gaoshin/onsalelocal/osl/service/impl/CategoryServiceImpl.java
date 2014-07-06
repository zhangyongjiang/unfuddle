package com.gaoshin.onsalelocal.osl.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.osl.beans.Category;
import com.gaoshin.onsalelocal.osl.dao.CategoryDao;
import com.gaoshin.onsalelocal.osl.service.CategoryService;

@Service("categoryService")
@Transactional(readOnly=true)
public class CategoryServiceImpl extends ServiceBaseImpl implements CategoryService {
    static final Logger logger = Logger.getLogger(CategoryServiceImpl.class);
    
	@Autowired private CategoryDao categoryDao;

	@Override
	public Category listTree() {
		return categoryDao.listTree();
	}

	@Override
    public Category nearby(Float latitude, Float longitude, Float radius, String merchant, Boolean localService, String category) {
		return categoryDao.nearby(latitude, longitude, radius, merchant, localService, category);
    }

	@Override
    public Category getTopCategories() {
		return categoryDao.getTopCategories();
    }

}
