package com.gaoshin.onsaleflyer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import common.db.dao.ConfigDao;
import common.db.dao.impl.CommonDaoImpl;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

public class ServiceBaseImpl {
    @Autowired protected CommonDaoImpl dao;
    @Autowired protected ConfigDao configDao;

    protected void assertNotNull(Object obj) {
    	if(obj == null)
    		throw new BusinessException(ServiceError.InvalidInput);
    }
}
