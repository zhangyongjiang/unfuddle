package com.gaoshin.points.server;

import com.gaoshin.points.server.bean.User;
import com.gaoshin.points.server.service.impl.UserServiceImpl;
import common.util.web.ResourceTester;

public class PointsResourceTester extends ResourceTester {
	
    protected User createUser(boolean isSuper) {
        User user = new User();
        String srandom = getCurrentTimeMillisString();
        user.setPhone(srandom);
        user.setName(isSuper ? (UserServiceImpl.SuperUserForTest + srandom) : srandom);
        user.setPassword(srandom);
        User created = getBuilder("/user/signup").post(User.class, user);
        created.setPassword(srandom);
        return created;
    }

    protected User login(User user) {
        User userLogin = getBuilder("/user/login").post(User.class, user);
        return userLogin;
    }

    protected void logout() {
        getBuilder("/user/logout").post(" ");
    }
    
    protected User getUserById(String userId) {
    	return getBuilder("/user/profile-by-id/" + userId).get(User.class);
    }
    
}
