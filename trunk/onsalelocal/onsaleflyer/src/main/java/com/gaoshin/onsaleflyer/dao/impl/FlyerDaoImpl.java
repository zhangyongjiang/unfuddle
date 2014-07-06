package com.gaoshin.onsaleflyer.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.gaoshin.onsaleflyer.dao.FlyerDao;
import common.db.dao.impl.GenericDaoImpl;

@Repository
public class FlyerDaoImpl extends GenericDaoImpl implements FlyerDao {
	private static final Logger log = Logger.getLogger(FlyerDaoImpl.class);
	
}
