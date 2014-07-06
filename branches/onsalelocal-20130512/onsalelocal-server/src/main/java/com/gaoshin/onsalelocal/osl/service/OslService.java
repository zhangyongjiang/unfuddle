package com.gaoshin.onsalelocal.osl.service;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.OfferDetails;
import com.gaoshin.onsalelocal.api.OfferDetailsList;
import com.gaoshin.onsalelocal.api.OfferList;
import com.gaoshin.onsalelocal.api.StoreList;
import com.gaoshin.onsalelocal.osl.entity.FavouriteOffer;
import com.gaoshin.onsalelocal.osl.entity.FavouriteStore;
import com.gaoshin.onsalelocal.osl.entity.FollowDetailsList;
import com.gaoshin.onsalelocal.osl.entity.OfferComment;
import com.gaoshin.onsalelocal.osl.entity.OfferCommentDetailsList;
import com.gaoshin.onsalelocal.osl.entity.StoreComment;
import com.gaoshin.onsalelocal.osl.entity.StoreCommentDetailsList;
import com.gaoshin.onsalelocal.osl.entity.StoreDetails;
import com.gaoshin.onsalelocal.osl.entity.UserList;

public interface OslService {

	OfferDetailsList searchOffer(float lat, float lng, int radius, int offset, int size);

	int harvest(String schema);

	void cleanup(int days);

	Offer getOffer(String offerId);

	StoreList nearbyStores(Float latitude, Float longitude, Float radius, String category, String subcategory, Boolean serviceOnly);

	OfferList trend(String userId, Float latitude, Float longitude, Integer radius, int offset, int size);

	OfferDetails getOfferDetails(String offerId);

	FavouriteOffer like(String userId, String offerId);

	UserList listLikedUserForOffer(String offerId);

	OfferComment comment(OfferComment comment);

	OfferCommentDetailsList listOfferComments(String offerId);

	void follw(String userId, String followerId);

	StoreDetails getStoreDetails(String storeId);

	FavouriteStore likeStore(String userId, String storeId);

	UserList listLikedUserForStore(String storeId);

	StoreComment commentStore(StoreComment comment);

	StoreCommentDetailsList listStoreComments(String storeId);

	OfferList listStoreOffers(String storeId);

	FollowDetailsList followers(String userId);

	FollowDetailsList followings(String userId);

	void submitOffer(Offer offer);

}
