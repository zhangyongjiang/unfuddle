package com.gaoshin.appbooster.dao.impl;

import java.util.Collections;

import org.springframework.stereotype.Repository;

import com.gaoshin.appbooster.dao.UserDao;
import com.gaoshin.appbooster.entity.User;

@Repository
public class UserDaoImpl extends GenericDaoImpl implements UserDao {
    
    @Override
    public User getUserByLogin(String login) {
        return getUniqueResult(User.class, Collections.singletonMap("login", login), "*");
    }
}
