package com.gaoshin.onsalelocal.osl.dao;

import java.util.List;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.osl.entity.FavouriteOffer;
import com.gaoshin.onsalelocal.osl.entity.User;
import common.db.dao.GenericDao;

public interface FavouriteDao extends GenericDao {

	List<FavouriteOffer> listByUserId(String userId);

	List<FavouriteOffer> listByOfferId(String offerId);

	List<User> listLikedUsers(String offerId);

	List<Offer> listUserLikedOffers(String userId);

	List<User> listLikedUsersForStore(String storeId);

}
