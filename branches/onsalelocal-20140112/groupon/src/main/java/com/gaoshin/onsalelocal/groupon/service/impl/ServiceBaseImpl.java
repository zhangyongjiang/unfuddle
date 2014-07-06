package com.gaoshin.onsalelocal.groupon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import common.db.dao.impl.CommonDaoImpl;

public class ServiceBaseImpl {
    @Autowired protected CommonDaoImpl dao;

}
