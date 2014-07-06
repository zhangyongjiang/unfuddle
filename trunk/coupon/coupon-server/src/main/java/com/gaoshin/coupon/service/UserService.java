package com.gaoshin.coupon.service;

import com.gaoshin.coupon.entity.User;

public interface UserService {
    User register(User user);

    User login(User user);

    User getUserById(String userId);

    User sendMobileVerifyCode(User user) throws Exception;
}
