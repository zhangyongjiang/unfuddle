package com.gaoshin.onsalelocal.osl.dao;

import java.util.List;
import java.util.Map;

import com.nextshopper.api.Offer;
import com.nextshopper.api.Store;
import com.nextshopper.osl.entity.Account;
import com.nextshopper.osl.entity.AccountType;
import com.nextshopper.osl.entity.User;
import com.nextshopper.osl.entity.UserDetails;

import common.db.dao.GenericDao;

public interface UserDao extends GenericDao {

	User getUserByLogin(String login);

	Account getAccount(String userId, AccountType gcm);

	int getFollowingCount(String userId);

	int getFollowerCount(String userId);

	int getOffersSubmitted(String userId);

	List<Offer> listOffersCreatedByUser(String userId, int offset, int size);

	List<Store> listStoresCreatedByUser(String userId);

	void enableNotification(String userId, boolean enable);

	List<User> listLatestUsers(int offset, int size);

	UserDetails getUserDetails(String userId);

	List<User> getUserList(List<String> userIds);

	Map<String, User> getUserMap(List<String> userIds);

	int getOffersLiked(String userId);

	int getStoresFollowing(String userId);

}
