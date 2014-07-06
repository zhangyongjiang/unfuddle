package com.gaoshin.appbooster.dao;

import com.gaoshin.appbooster.entity.User;

public interface UserDao extends GenericDao {
    User getUserByLogin(String login);
}
