package com.gaoshin.onsalelocal.osl.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.osl.beans.Category;
import com.gaoshin.onsalelocal.osl.dao.OslCategoryDao;
import com.gaoshin.onsalelocal.osl.service.OslCategoryService;

@Service("oslCategoryService")
@Transactional(readOnly=true)
public class OslCategoryServiceImpl extends ServiceBaseImpl implements OslCategoryService {
    static final Logger logger = Logger.getLogger(OslCategoryServiceImpl.class);
    
	@Autowired private OslCategoryDao categoryDao;

	@Override
    public Category nearby(Float latitude, Float longitude, Float radius, String merchant, Boolean localService, String category) {
		return categoryDao.nearby(latitude, longitude, radius, merchant, localService, category);
    }

	@Override
    public Category getTopCategories() {
		return categoryDao.getTopCategories();
    }

}
