package com.gaoshin.points.server.service;

import com.gaoshin.points.server.bean.User;
import com.gaoshin.points.server.bean.UserBalanceList;

public interface UserService {

	User signup(User user);

	User login(User user);

	User getUserById(String userId);

	UserBalanceList listBalance(String userId);

}
