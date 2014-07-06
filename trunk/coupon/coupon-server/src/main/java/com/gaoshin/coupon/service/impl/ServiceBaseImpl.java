package com.gaoshin.coupon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.gaoshin.coupon.dao.impl.CommonDaoImpl;
import com.gaoshin.coupon.service.ServiceBase;

public class ServiceBaseImpl implements ServiceBase {
    @Autowired private CommonDaoImpl dao;

    @Override
    public <T> T getEntity(Class<T> class1, String id) {
        return dao.getEntity(class1, id, "*");
    }

}
