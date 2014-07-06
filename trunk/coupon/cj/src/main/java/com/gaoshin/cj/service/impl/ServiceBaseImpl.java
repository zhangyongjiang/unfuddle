package com.gaoshin.cj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.gaoshin.cj.dao.impl.CommonDaoImpl;


public class ServiceBaseImpl {
    @Autowired protected CommonDaoImpl dao;

}
