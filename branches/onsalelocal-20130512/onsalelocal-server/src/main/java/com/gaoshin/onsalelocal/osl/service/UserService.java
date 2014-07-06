package com.gaoshin.onsalelocal.osl.service;

import com.gaoshin.onsalelocal.osl.entity.Account;
import com.gaoshin.onsalelocal.osl.entity.AccountType;
import com.gaoshin.onsalelocal.osl.entity.User;
import com.gaoshin.onsalelocal.osl.entity.UserDetails;

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

}
