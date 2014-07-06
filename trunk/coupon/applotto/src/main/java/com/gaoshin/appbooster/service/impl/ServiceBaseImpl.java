package com.gaoshin.appbooster.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.gaoshin.appbooster.dao.impl.CommonDaoImpl;
import com.gaoshin.appbooster.service.ServiceBase;

public class ServiceBaseImpl implements ServiceBase {
    @Autowired private CommonDaoImpl dao;

    @Override
    public <T> T getEntity(Class<T> class1, String id) {
        return dao.getEntity(class1, id, "*");
    }

}
