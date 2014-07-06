package com.gaoshin.onsaleflyer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsaleflyer.dao.OfferDao;
import com.gaoshin.onsaleflyer.service.OfferService;

@Service
@Transactional(readOnly=true)
public class OfferServiceImpl extends ServiceBaseImpl implements OfferService {
    @Autowired private OfferDao offerDao;
    
}
