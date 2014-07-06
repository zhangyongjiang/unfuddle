package com.gaoshin.onsalelocal.osl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import common.db.dao.impl.CommonDaoImpl;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

public class ServiceBaseImpl {
    @Autowired protected CommonDaoImpl dao;

    protected void assertNotNull(Object obj) {
    	if(obj == null)
    		throw new BusinessException(ServiceError.InvalidInput);
    }

    protected void assertNotNull(Object obj, String fieldName) {
    	if(obj == null)
    		throw new BusinessException(ServiceError.InvalidInput, fieldName + " should not be null");
    }
}
