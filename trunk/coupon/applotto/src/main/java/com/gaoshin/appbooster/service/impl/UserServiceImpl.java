package com.gaoshin.appbooster.service.impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.appbooster.dao.UserDao;
import com.gaoshin.appbooster.entity.UserStatus;
import com.gaoshin.appbooster.entity.User;
import com.gaoshin.appbooster.service.UserService;
import com.gaoshin.authone.service.TwilioService;
import common.util.MD5;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

@Service
@Transactional(readOnly=true)
public class UserServiceImpl extends ServiceBase implements UserService {
    @Autowired private UserDao userDao;
    @Autowired private TwilioService twilioService;
    
    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public User register(User user) {
        user.setId(null);
        assertNotNull(user.getPassword());
        assertNotNull(user.getLogin());
        
        User indb = userDao.getUserByLogin(user.getLogin());
        if(indb != null) {
            throw new BusinessException(ServiceError.Duplicated);
        }
        
        user.setPassword(MD5.md5(user.getPassword()));
        user.setStatus(UserStatus.Registered);
        userDao.insert(user);
        
        return user;
    }

    @Override
    public User login(User user) {
        assertNotNull(user.getLogin());
        assertNotNull(user.getPassword());
        String login = user.getLogin();
        User dbuser = userDao.getUniqueResult(User.class, Collections.singletonMap("login", login), "id, password");
        if(dbuser == null)
            throw new BusinessException(ServiceError.NotFound);
        if(!MD5.md5(user.getPassword()).equals(dbuser.getPassword()) && !user.getPassword().equals(dbuser.getPassword()))
            throw new BusinessException(ServiceError.InvalidInput, "Invalid password");
        return dbuser;
    }

    @Override
    public User getUserById(String userId) {
        return userDao.getEntity(User.class, userId);
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void sendMobileVerifyCode(String userId, String phone) throws Exception {
        String token = twilioService.sendMsg(phone);
        userDao.update(User.class, Collections.singletonMap("validationCode", token), Collections.singletonMap("id", userId));
    }

    @Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public void verify(String userId, String code) {
        User dbuser = userDao.getEntity(User.class, userId, "validationCode");
        if(!code.equals(dbuser.getValidationCode())) {
            throw new BusinessException(ServiceError.InvalidInput);
        }
        dbuser.setStatus(UserStatus.Verified);
        userDao.updateEntity(dbuser, "status");
    }

}
