package com.gaoshin.appbooster.webservice;

import com.gaoshin.appbooster.entity.User;
import com.gaoshin.appbooster.entity.UserType;
import common.util.web.ResourceTester;

public class ApplottoTestBase extends ResourceTester {
    
    public User registerUser(UserType userType, String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        User registered = getBuilder("/ws/user/register").post(User.class, user);
        getBuilder("/ws/user/login").post(user);
        return registered;
    }
    
    public void loginUser(User user) {
        getBuilder("/ws/user/login").post(user);
    }
    
    public void logoutUser() {
        getBuilder("/ws/user/logout").post(" ");
    }
    
}
