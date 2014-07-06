package com.gaoshin.points.server.dao;

import com.gaoshin.points.server.entity.UserEntity;

public interface UserDao extends GenericDao {

	UserEntity findUserByPhone(String phone);

}
