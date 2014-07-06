package com.gaoshin.onsaleflyer.resource;

import org.junit.Before;

import com.gaoshin.onsaleflyer.entity.User;
import common.util.web.ResourceTester;

public class LoginUserResourceTester extends ResourceTester {
	protected User user;
	
	@Before
	public void register() {
		user = new User();
		user.setName(getRandomString(3, 8));
		user.setLogin(getRandomString(3, 8));
		user.setPassword(getRandomString(4, 9));
		User registered = getBuilder("/ws/user/register").post(User.class, user);
		user.setId(registered.getId());
	}
	
	protected void logout() {
		getBuilder("/ws/user/logout").post();
	}
}
