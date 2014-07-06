package com.gaoshin.onsalelocal.osl.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.api.Category;
import com.gaoshin.onsalelocal.api.CategoryBean;
import com.gaoshin.onsalelocal.api.dao.CategoryDao;
import com.gaoshin.onsalelocal.osl.service.OslCategoryService;

@Service("oslCategoryService")
@Transactional(readOnly=true)
public class OslCategoryServiceImpl extends ServiceBaseImpl implements OslCategoryService {
    static final Logger logger = Logger.getLogger(OslCategoryServiceImpl.class);
    
	@Autowired private CategoryDao categoryDao;

	@Override
    public CategoryBean getTopCategories() {
		return categoryDao.getTopCategories();
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void create(Category cat) {
		categoryDao.insert(cat);
    }

}
