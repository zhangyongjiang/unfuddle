package com.gaoshin.onsalelocal.osl.dao;

import java.util.List;

import com.nextshopper.api.Offer;
import com.nextshopper.api.Store;
import com.nextshopper.osl.entity.FavouriteOffer;
import com.nextshopper.osl.entity.User;

import common.db.dao.GenericDao;

public interface FavouriteDao extends GenericDao {

	List<FavouriteOffer> listByUserId(String userId);

	List<FavouriteOffer> listByOfferId(String offerId);

	List<User> listLikedUsers(String offerId);

	List<Offer> listUserLikedOffers(String userId);

	List<Store> listUserLikedStores(String userId);

	List<User> listLikedUsersForStore(String storeId);

	List<User> listCommentedUsersForStore(String storeId);

	List<User> listCommentedUsersForOffer(String offerId);

	boolean isLiked(String userId, String offerId);

}
