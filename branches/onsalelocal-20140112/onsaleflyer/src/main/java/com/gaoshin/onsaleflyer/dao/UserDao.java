package com.gaoshin.onsaleflyer.dao;

import com.gaoshin.onsaleflyer.entity.User;
import common.db.dao.GenericDao;

public interface UserDao extends GenericDao {

	User getUserByLogin(String login);

}
