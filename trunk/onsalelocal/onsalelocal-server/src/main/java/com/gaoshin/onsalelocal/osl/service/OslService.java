package com.gaoshin.onsalelocal.osl.service;

import java.util.List;

import com.nextshopper.api.Company;
import com.nextshopper.api.Offer;
import com.nextshopper.api.OfferDetails;
import com.nextshopper.api.OfferDetailsList;
import com.nextshopper.api.Store;
import com.nextshopper.api.StoreList;
import com.nextshopper.api.UserOfferDetailsList;
import com.nextshopper.osl.entity.FavouriteOffer;
import com.nextshopper.osl.entity.FavouriteOfferDetailsList;
import com.nextshopper.osl.entity.FavouriteStore;
import com.nextshopper.osl.entity.FavouriteStoreDetailsList;
import com.nextshopper.osl.entity.Follow;
import com.nextshopper.osl.entity.FollowDetailsList;
import com.nextshopper.osl.entity.FollowStatus;
import com.nextshopper.osl.entity.NotificationDetailList;
import com.nextshopper.osl.entity.OfferComment;
import com.nextshopper.osl.entity.OfferCommentDetailsList;
import com.nextshopper.osl.entity.StoreComment;
import com.nextshopper.osl.entity.StoreCommentDetailsList;
import com.nextshopper.osl.entity.StoreDetails;
import com.nextshopper.osl.entity.StoreDetailsList;
import com.nextshopper.osl.entity.TagList;
import com.nextshopper.osl.entity.UserDetailsList;
import com.nextshopper.osl.entity.UserList;

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

	Follow follw(String userId, String followerId);

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

	Offer getCompanyOffer(String offerId);

	List<Company> searchCompanies(String keywords, int offset, int size);

	void follow(String followerId, String userId, FollowStatus approved);

}
