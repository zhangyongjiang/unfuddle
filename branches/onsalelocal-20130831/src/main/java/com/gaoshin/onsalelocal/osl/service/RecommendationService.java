package com.gaoshin.onsalelocal.osl.service;

import java.util.List;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.osl.beans.Category;
import com.gaoshin.onsalelocal.osl.entity.FavoriteCategory;

public interface RecommendationService {
	void addUserInterest(String userId, String offerId);
	List<Offer> getRecommendations(String userId);
	List<Offer> getRecommendations(String userId, String offerId);
	void setFavoriteCategories(String userId, List<Category> items);
	List<FavoriteCategory> getFavoriteCategories(String userId);
}
