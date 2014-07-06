package com.gaoshin.appbooster.service;

import com.gaoshin.appbooster.entity.User;

public interface UserService {
    User register(User user);

    User login(User user);

    User getUserById(String userId);

    void sendMobileVerifyCode(String userId, String phone) throws Exception;

    void verify(String userId, String code);
}
