package com.gaoshin.coupon.webservice;

import com.gaoshin.coupon.entity.User;
import com.gaoshin.coupon.entity.UserType;
import common.util.web.ResourceTester;

public class CouponeTestBase extends ResourceTester {
    public User register(UserType userType) {
        String login = getCurrentTimeMillisString();
        return register(userType, login);
    }
    
    public User register(UserType userType, String login) {
        String password = getCurrentTimeMillisString();
        return register(userType, login, password);
    }
    
    public void setTestLocation() {
        getBuilder("/ws/location/set-location/CA").get(String.class);
    }
    
    public User register(UserType userType, String login, String password) {
        setTestLocation();
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        User registered = getBuilder("/ws/user/register").post(User.class, user);
        getBuilder("/ws/user/login").post(user);
        return registered;
    }
    
    public void login(User user) {
        getBuilder("/ws/user/login").post(user);
    }
    
    public void logout() {
        getBuilder("/ws/user/logout").post(" ");
    }
}
