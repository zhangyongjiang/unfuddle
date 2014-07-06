package com.gaoshin.onsaleflyer.dao.impl;

import java.util.Collections;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gaoshin.onsaleflyer.dao.UserDao;
import com.gaoshin.onsaleflyer.entity.User;
import common.db.dao.ConfigDao;
import common.db.dao.impl.GenericDaoImpl;

@Repository
public class UserDaoImpl extends GenericDaoImpl implements UserDao {
	private static final Logger log = Logger.getLogger(UserDaoImpl.class);
	
	@Autowired private ConfigDao configDao;

	@Override
	public User getUserByLogin(String login) {
		return getUniqueResult(User.class, Collections.singletonMap("login", login));
	}

}
