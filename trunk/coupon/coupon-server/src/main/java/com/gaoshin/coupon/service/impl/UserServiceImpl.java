package com.gaoshin.coupon.service.impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.authone.service.TwilioService;
import com.gaoshin.coupon.dao.UserDao;
import com.gaoshin.coupon.entity.AccountStatus;
import com.gaoshin.coupon.entity.User;
import com.gaoshin.coupon.service.UserService;
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
        user.setStatus(AccountStatus.Active);
        userDao.insert(user);
        
        if(user.getLogin().indexOf("transaction-test") !=-1) {
            throw new BusinessException(ServiceError.InvalidInput);
        }
        
        return user;
    }

    @Override
    public User login(User user) {
        assertNotNull(user.getLogin());
        assertNotNull(user.getPassword());
        String login = user.getLogin();
        User dbuser = userDao.getUniqueResult(User.class, Collections.singletonMap("login", login), "id, password,mobilePassword");
        if(dbuser == null)
            throw new BusinessException(ServiceError.NotFound);
        if(!MD5.md5(user.getPassword()).equals(dbuser.getPassword()) && !user.getPassword().equals(dbuser.getPassword()))
            throw new BusinessException(ServiceError.InvalidInput, "Invalid password");
//        if(user.getMobilePassword() == null || !user.getMobilePassword().equals(dbuser.getMobilePassword()))
//            throw new BusinessException(ServiceError.InvalidInput, "Invalid mobile valiation code.");
        return dbuser;
    }

    @Override
    public User getUserById(String userId) {
        return userDao.getEntity(User.class, userId);
    }

    @Override
    public User sendMobileVerifyCode(User user) throws Exception {
        String login = user.getLogin();
        User db = userDao.getUniqueResult(User.class, Collections.singletonMap("login", login), "id, password,mobile");
        if(db == null)
            throw new BusinessException(ServiceError.NotFound);
        if(!MD5.md5(user.getPassword()).equals(db.getPassword()) && !user.getPassword().equals(db.getPassword()))
            throw new BusinessException(ServiceError.InvalidInput);
        
        String phone = db.getMobile();
        if("msg".equals(user.getMobile())) {
            String token = twilioService.sendMsg(phone);
            db.setMobilePassword(token);
            userDao.updateEntity(db, "mobilePassword");
        }
        else {
            String token = twilioService.call(phone);
            db.setMobilePassword(token);
            userDao.updateEntity(db, "mobilePassword");
        }
        
        return user;
    }

}
