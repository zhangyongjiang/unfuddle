package com.gaoshin.onsalelocal.osl.dao;

import com.gaoshin.onsalelocal.osl.entity.Account;
import com.gaoshin.onsalelocal.osl.entity.AccountType;
import com.gaoshin.onsalelocal.osl.entity.User;
import common.db.dao.GenericDao;

public interface UserDao extends GenericDao {

	User getUserByLogin(String login);

	Account getAccount(String userId, AccountType gcm);

	int getFollowingCount(String userId);

	int getFollowerCount(String userId);

	int getOffersSubmitted(String userId);

}
