package com.gaoshin.onsalelocal.osl.service;

import java.util.List;

import com.gaoshin.onsalelocal.api.Company;
import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.OfferDetails;
import com.gaoshin.onsalelocal.api.OfferDetailsList;
import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.api.StoreList;
import com.gaoshin.onsalelocal.api.UserOfferDetailsList;
import com.gaoshin.onsalelocal.osl.entity.FavouriteOffer;
import com.gaoshin.onsalelocal.osl.entity.FavouriteOfferDetailsList;
import com.gaoshin.onsalelocal.osl.entity.FavouriteStore;
import com.gaoshin.onsalelocal.osl.entity.FavouriteStoreDetailsList;
import com.gaoshin.onsalelocal.osl.entity.FollowDetailsList;
import com.gaoshin.onsalelocal.osl.entity.NotificationDetailList;
import com.gaoshin.onsalelocal.osl.entity.OfferComment;
import com.gaoshin.onsalelocal.osl.entity.OfferCommentDetailsList;
import com.gaoshin.onsalelocal.osl.entity.StoreComment;
import com.gaoshin.onsalelocal.osl.entity.StoreCommentDetailsList;
import com.gaoshin.onsalelocal.osl.entity.StoreDetails;
import com.gaoshin.onsalelocal.osl.entity.StoreDetailsList;
import com.gaoshin.onsalelocal.osl.entity.TagList;
import com.gaoshin.onsalelocal.osl.entity.UserDetailsList;
import com.gaoshin.onsalelocal.osl.entity.UserList;

public interface OslService {

	OfferDetailsList searchOffer(float lat, float lng, int radius, int offset, int size);

	int harvest(String schema);

	void cleanup(int days);

	Offer getOffer(String offerId);

	StoreList nearbyStores(Float latitude, Float longitude, Float radius, boolean hasOffer);

	OfferDetailsList trend(String userId, Float latitude, Float longitude, Integer radius, int offset, int size);

	OfferDetails getOfferDetails(String userId, String offerId);

	FavouriteOffer like(String userId, String offerId);

	UserList listLikedUserForOffer(String offerId);

	OfferComment comment(OfferComment comment);

	OfferCommentDetailsList listOfferComments(String userId, String offerId);

	void follw(String userId, String followerId);

	StoreDetails getStoreDetails(String storeId);

	FavouriteStore followStore(String userId, String storeId);

	UserDetailsList listLikedUserForStore(String storeId, String userId);

	StoreComment commentStore(StoreComment comment);

	StoreCommentDetailsList listStoreComments(String storeId);

	OfferDetailsList listStoreOffers(String storeId, String userId, int offset, int size);

	FollowDetailsList followers(String userId, String currentUserId);

	FollowDetailsList followings(String userId, String currentUserId);

	void submitOffer(Offer offer);

	FavouriteOfferDetailsList listOffersLikedByUser(String userId, String currentUserId);

	FavouriteStoreDetailsList listStoresLikedByUser(String userId, String currentUserId);

	OfferDetailsList listOffersCreatedByUser(String userId, int offset, int size, String currentUserId);

	Store createStore(String userId, Store store);

	void updateStoreImageUrl(String storeId, String imgUrl);

	StoreDetailsList listStoresCreatedByUser(String userId, String currentUserId);

	Store updateStore(String userId, Store store);

	TagList listTags();

	void deleteOffer(String offerId);

	int importYipitOffer(int size);

	List<OfferDetails> decorateOffers(List<? extends Offer> offers,
            String userId);

	NotificationDetailList listUserNotifications(String userId, int offset, int size);

	void unfollowStore(String userId, String storeId);

	void unlike(String userId, String offerId);

	void unfollow(String userId, String followerId);

	void copyOffer(Offer offer);

	boolean isFollowing(String me, String userId);

	UserOfferDetailsList followingsDeals(String userId, int offset, int size);

	boolean isFollowingStore(String storeId, String userId);

	OfferDetailsList listOfferDetailByIds(String userId, List<String> ids,
            Float latitude, Float longitude);

	void flagUser(String meId, String userId);

	void flagOffer(String meId, String offerId);

	void markAllNotificationsRead(String userId);

	Company createCompany(String userId, Company company);

	Company updateCompany(String userId, Company company);

	Company getCompany(String companyId);

	List<Company> listCompanies(int offset, int size);

}
