package com.gaoshin.cloud.web.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.cloud.web.bean.BusinessException;
import com.gaoshin.cloud.web.bean.ServiceError;
import com.gaoshin.cloud.web.user.bean.User;
import com.gaoshin.cloud.web.user.dao.UserDao;
import com.gaoshin.cloud.web.user.entity.UserAuthority;
import com.gaoshin.cloud.web.user.entity.UserEntity;
import com.gaoshin.cloud.web.user.entity.UserRole;
import common.util.MD5;
import common.util.reflection.ReflectionUtil;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User signup(User user) {
        UserEntity entity = userDao.getUniqueResult(UserEntity.class, "username", user.getUsername());
        if(entity != null) {
            throw new BusinessException(ServiceError.Duplicated, "user name " + user.getUsername() + " already exists in our system");
        }
        
        entity = userDao.getUniqueResult(UserEntity.class, "email", user.getEmail());
        if(entity != null) {
            throw new BusinessException(ServiceError.Duplicated, "user email " + user.getEmail() + " already exists in our system");
        }
        
        entity = userDao.getUniqueResult(UserEntity.class, "displayName", user.getDisplayName());
        if(entity != null) {
            throw new BusinessException(ServiceError.Duplicated, "display name " + user.getDisplayName() + " already exists in our system");
        }
        
        entity = ReflectionUtil.copy(UserEntity.class, user);
        entity.setPassword(MD5.md5(entity.getPassword()));
        userDao.saveEntity(entity);
        
        addAuthority(user.getUsername(), UserRole.User);
        if(user.getEmail().contains(".gao")) {
            addAuthority(user.getUsername(), UserRole.Admin);
        }
        if(user.getEmail().contains(".gaoshin")) {
            addAuthority(user.getUsername(), UserRole.Super);
        }
        
        return user;
    }
    
    private void addAuthority(String userName, UserRole role) {
        UserAuthority authority = new UserAuthority();
        authority.setUsername(userName);
        authority.setAuthority(role.name());
        userDao.saveEntity(authority);
    }
}
