package com.gaoshin.points.server.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gaoshin.points.server.bean.User;
import com.gaoshin.points.server.bean.UserBalance;
import com.gaoshin.points.server.bean.UserBalanceList;
import com.gaoshin.points.server.dao.UserDao;
import com.gaoshin.points.server.entity.UserBalanceEntity;
import com.gaoshin.points.server.entity.UserEntity;
import com.gaoshin.points.server.entity.UserStatus;
import com.gaoshin.points.server.entity.UserType;
import com.gaoshin.points.server.service.UserService;
import common.util.MD5;
import common.util.reflection.ReflectionUtil;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

@Service("userService")
public class UserServiceImpl extends ServiceBase implements UserService {
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
	
	public static final String SuperUserForTest = "__SUPER__";

	@Autowired private UserDao userDao;
	
	@Override
	public User signup(User user) {
		UserEntity existing = userDao.findUserByPhone(user.getPhone());
		if(existing != null) {
			throw new BusinessException(ServiceError.Duplicated);
		}
		
		UserEntity ue = ReflectionUtil.copy(UserEntity.class, user);
		ue.setPassword(MD5.md5(user.getPassword()));
		ue.setUserStatus(UserStatus.Registered);
		
		if(user.getName().startsWith(SuperUserForTest) && getBooleanProperty("FOR_TEST", false)) {
			ue.setUserType(UserType.Super);
		}
		else {
			ue.setUserType(UserType.Individual);
		}
		
		userDao.insertEntity(ue);
		
		return new User(ue);
	}

	@Override
	public User login(User user) {
		UserEntity existing = userDao.findUserByPhone(user.getPhone());
		if(existing == null) {
			throw new BusinessException(ServiceError.NotFound);
		}
		
		if(!MD5.md5(user.getPassword()).equals(existing.getPassword())) {
			throw new BusinessException(ServiceError.InvalidInput);
		}
		
		return new User(existing);
	}

	@Override
	public User getUserById(String userId) {
		return new User(userDao.getEntity(UserEntity.class, userId));
	}

	@Override
	public UserBalanceList listBalance(String userId) {
		UserBalanceList ubl = new UserBalanceList();
		List<UserBalanceEntity> entities = userDao.list(UserBalanceEntity.class, "userId", userId);
		for(UserBalanceEntity ube : entities) {
			ubl.getList().add(new UserBalance(ube));
		}
		return ubl;
	}

}
