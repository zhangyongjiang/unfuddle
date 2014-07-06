package com.gaoshin.onsalelocal.osl.dao;

import java.util.List;

import com.nextshopper.api.Category;
import com.nextshopper.api.Offer;
import com.nextshopper.osl.entity.FavoriteCategory;

import common.db.dao.GenericDao;

public interface RecommendationDao extends GenericDao {

	void addUserInterest(String userId, String offerId);

	List<FavoriteCategory> getFavoriteCategories(String userId);

	void setFavoriteCategories(String userId, List<Category> items);

	List<Offer> getRecommendations(String userId);

	List<Offer> getRecommendations(String userId, String offerId);
}
