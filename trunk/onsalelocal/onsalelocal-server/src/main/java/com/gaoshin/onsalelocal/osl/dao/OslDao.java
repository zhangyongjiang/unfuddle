package com.gaoshin.onsalelocal.osl.dao;

import java.util.List;
import java.util.Map;

import com.nextshopper.api.Offer;
import com.nextshopper.api.Sort;
import com.nextshopper.api.Store;
import com.nextshopper.api.StoreList;
import com.nextshopper.osl.entity.Notification;
import com.nextshopper.osl.entity.Tag;
import com.nextshopper.osl.entity.User;

import common.db.dao.City;
import common.db.dao.GenericDao;

public interface OslDao extends GenericDao {

	List<Offer> listOffersIn(float lat, float lng, int radius, int offset, int size, String source, Sort sort);

	int harvest(String schema);

	void cleanup(int days);

	void ondemandShoplocal(City city);

	StoreList nearbyStores(Float latitude, Float longitude, Float radius, boolean hasOffer);

	List<Offer> listStoreOffers(String storeId, int offset, int size);

	List<Tag> listTags();

	List<Offer> trend(float lat, float lng, int radius, int offset, int size);

	Store searchStore(String source, String sourceId, Store store);

	List<Offer> followingsDeals(String userId, int offset, int size);

	List<Notification> getLatestNotifications(String userId, int offset,
            int size);

	Map<String, Store> getStoreMap(List<String> storeIds);

	Map<String, Offer> getOfferMap(List<String> offerIds);

	List<Store> getStoreList(List<String> storeIds);

	List<Offer> getOfferList(List<String> offerIds);

	List<User> listFollowers(String userId);

	List<User> followingUsers(String userId);

	Map<String, User> followingUserMap(String userId);

	void markAllNotificationsRead(String userId);

	int getTotalNotifications(String userId);

	int getTotalUnreadNotifications(String userId);

}
