package com.gaoshin.onsalelocal.osl.dao.impl;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.gaoshin.onsalelocal.api.Category;
import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.osl.dao.RecommendationDao;
import com.gaoshin.onsalelocal.osl.entity.FavoriteCategory;
import com.gaoshin.onsalelocal.osl.entity.UserInterest;
import common.db.dao.impl.GenericDaoImpl;

@Repository("recommendationDao")
public class RecommendationDaoImpl extends GenericDaoImpl implements RecommendationDao {
	private static final Logger log = Logger.getLogger(RecommendationDaoImpl.class);

	@Override
    public void addUserInterest(String userId, String offerId) {
		UserInterest ui = new UserInterest();
		ui.setUserId(userId);
		ui.setOfferId(offerId);
		insert(ui);
    }

	@Override
    public List<FavoriteCategory> getFavoriteCategories(String userId) {
	    return query(FavoriteCategory.class, Collections.singletonMap("userId", userId));
    }

	@Override
    public void setFavoriteCategories(String userId, List<Category> items) {
		for(Category cat : items) {
			FavoriteCategory fc = new FavoriteCategory();
			fc.setUserId(userId);
			fc.setCategoryId(cat.getId());
			insert(fc, true);
		}
    }

	@Override
    public List<Offer> getRecommendations(String userId) {
	    return null;
    }

	@Override
    public List<Offer> getRecommendations(String userId, String offerId) {
	    return null;
    }
	
}
