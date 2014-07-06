package com.gaoshin.onsalelocal.osl.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.api.Category;
import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.osl.dao.RecommendationDao;
import com.gaoshin.onsalelocal.osl.entity.FavoriteCategory;
import com.gaoshin.onsalelocal.osl.service.RecommendationService;

@Service("recommendationService")
@Transactional(readOnly=true)
public class RecommendationServiceImpl extends ServiceBaseImpl implements RecommendationService {
    static final Logger logger = Logger.getLogger(SearchServiceImpl.class);
    
	@Autowired private RecommendationDao dao;
	
	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void addUserInterest(String userId, String offerId) {
		dao.addUserInterest(userId, offerId);
    }
	
	@Override
    public List<Offer> getRecommendations(String userId) {
	    return dao.getRecommendations(userId);
    }
	
	@Override
    public List<Offer> getRecommendations(String userId, String offerId) {
	    return dao.getRecommendations(userId, offerId);
    }
	
	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void setFavoriteCategories(String userId, List<Category> items) {
	    dao.setFavoriteCategories(userId, items);
    }
	
	@Override
    public List<FavoriteCategory> getFavoriteCategories(String userId) {
	    return dao.getFavoriteCategories(userId);
    }
}
