package com.gaoshin.onsaleflyer.service;

import com.gaoshin.onsaleflyer.beans.PasswordChangeRequest;
import com.gaoshin.onsaleflyer.entity.User;

public interface UserService {

	User getUserById(String userId);

	User login(User user);

	User register(User user);

	void update(User user);

	void changePassword(String userId, PasswordChangeRequest req);

}
