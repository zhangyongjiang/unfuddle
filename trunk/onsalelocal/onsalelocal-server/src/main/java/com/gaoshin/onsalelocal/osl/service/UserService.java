package com.gaoshin.onsalelocal.osl.service;

import com.nextshopper.osl.entity.Account;
import com.nextshopper.osl.entity.AccountType;
import com.nextshopper.osl.entity.OfferComment;
import com.nextshopper.osl.entity.User;
import com.nextshopper.osl.entity.UserDetails;
import com.nextshopper.osl.entity.UserFileList;
import com.nextshopper.osl.entity.UserList;

public interface UserService {
    User register(User user);

    User login(User user);

    User getUserById(String userId);

	void update(User user);

	void subscribe(String userId, String email);

	User facebookLogin(Account account) throws Exception;

	void resetPassword(String email);

	void registerPushNotificationId(String userId, AccountType type, String pnid);

	UserDetails getUserDetailsById(String userId);

	UserFileList listFiles(String userId);

	Account getAccount(String userId, AccountType apn);

	void enableNotification(String userId, boolean enable);

	UserList listLatestUsers(int offset, int size);

	void postToFacebook(String userId, OfferComment comment);

}
