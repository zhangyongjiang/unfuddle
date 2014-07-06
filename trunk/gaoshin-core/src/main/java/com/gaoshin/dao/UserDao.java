package com.gaoshin.dao;

import com.gaoshin.beans.User;
import com.gaoshin.dao.jpa.DaoComponent;
import com.gaoshin.entity.UserEntity;

public interface UserDao extends DaoComponent {

    UserEntity getUser(User user);
}
