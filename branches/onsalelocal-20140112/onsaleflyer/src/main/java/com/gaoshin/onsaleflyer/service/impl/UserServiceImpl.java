package com.gaoshin.onsaleflyer.service.impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsaleflyer.beans.PasswordChangeRequest;
import com.gaoshin.onsaleflyer.dao.UserDao;
import com.gaoshin.onsaleflyer.entity.User;
import com.gaoshin.onsaleflyer.entity.UserStatus;
import com.gaoshin.onsaleflyer.service.UserService;
import common.util.MD5;
import common.util.StringUtil;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

@Service
@Transactional(readOnly=true)
public class UserServiceImpl extends ServiceBaseImpl implements UserService {
    @Autowired private UserDao userDao;
    
    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public User register(User user) {
    	user.setId(null);
    	user.setLogin(StringUtil.nullIfEmpty(user.getLogin()));
    	user.setPassword(StringUtil.nullIfEmpty(user.getPassword()));
    	if(user.getLogin() == null)
    		throw new BusinessException(ServiceError.InvalidInput, "The login field should not be null");
    	if(user.getPassword() == null)
    		throw new BusinessException(ServiceError.InvalidInput, "The password field should not be null");
    	String pwd = user.getPassword();
    	String md5 = MD5.md5(pwd);
    	user.setPassword(md5);
    	user.setStatus(UserStatus.Active);
    	userDao.insert(user);
    	user.setPassword(null);
    	
        return user;
    }

    @Override
    public User login(User user) {
        assertNotNull(user.getLogin());
        assertNotNull(user.getPassword());
        String login = user.getLogin();
        User dbuser = userDao.getUniqueResult(User.class, Collections.singletonMap("login", login));
        if(dbuser == null)
            throw new BusinessException(ServiceError.NotFound);
        if(!MD5.md5(user.getPassword()).equals(dbuser.getPassword()) && !user.getPassword().equals(dbuser.getPassword()))
            throw new BusinessException(ServiceError.InvalidInput, "Invalid password");
        dbuser.setPassword(user.getPassword());
        return dbuser;
    }

    @Override
    public User getUserById(String userId) {
        return userDao.getEntity(User.class, userId);
    }

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void update(User user) {
		User db = getUserById(user.getId());
		db.setAdddress(user.getAdddress());
		db.setAdddress2(user.getAdddress2());
		db.setCity(user.getCity());
		db.setCountry(user.getCountry());
		db.setEmail(user.getEmail());
		db.setLogo(user.getLogo());
		db.setName(user.getName());
		db.setPhone(user.getPhone());
		db.setState(user.getState());
		db.setUrl(user.getUrl());
		userDao.updateEntity(db);
	}

	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void changePassword(String userId, PasswordChangeRequest req) {
		User db = getUserById(userId);
		if(db.getPassword() == null && req.getOldPwd() != null)
			throw new BusinessException(ServiceError.InvalidInput, "Wrong old password");
		if(db.getPassword() != null && req.getOldPwd() == null)
			throw new BusinessException(ServiceError.InvalidInput, "Wrong old password");
		if(db.getPassword() != null && req.getOldPwd() != null && !MD5.md5(req.getOldPwd()).equals(db.getPassword()))
			throw new BusinessException(ServiceError.InvalidInput, "Wrong old password");
		
		db.setPassword(MD5.md5(req.getNewPwd()));
		userDao.updateEntity(db, "password");
	}

}
