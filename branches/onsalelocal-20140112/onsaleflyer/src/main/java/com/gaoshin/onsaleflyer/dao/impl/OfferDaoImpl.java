package com.gaoshin.onsaleflyer.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.gaoshin.onsaleflyer.dao.OfferDao;
import common.db.dao.impl.GenericDaoImpl;

@Repository
public class OfferDaoImpl extends GenericDaoImpl implements OfferDao {
	private static final Logger log = Logger.getLogger(OfferDaoImpl.class);
	
}
