package com.gaoshin.onsaleflyer.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsaleflyer.dao.FlyerDao;
import com.gaoshin.onsaleflyer.service.FlyerService;

@Service
@Transactional(readOnly=true)
public class FlyerServiceImpl extends ServiceBaseImpl implements FlyerService {
    @Autowired private FlyerDao flyerDao;
	
    private String flyerHome;
    
    @PostConstruct
    public void init() {
    	flyerHome = configDao.get("flyer.home");
    }
    
    
}
