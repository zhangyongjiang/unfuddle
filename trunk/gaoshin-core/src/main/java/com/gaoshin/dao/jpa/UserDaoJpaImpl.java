package com.gaoshin.dao.jpa;

import org.springframework.stereotype.Repository;

import com.gaoshin.beans.User;
import com.gaoshin.dao.UserDao;
import com.gaoshin.entity.UserEntity;

@Repository("userDao")
public class UserDaoJpaImpl extends DaoComponentImpl implements UserDao {

    @Override
    public void init() {
    }

    @Override
    public UserEntity getUser(User user) {
        if (user == null)
            return null;

        UserEntity entity = null;
        if (entity == null && user.getId() != null)
            entity = getEntity(UserEntity.class, user.getId());
        if (entity == null && user.getName() != null)
            entity = getFirstEntityBy(UserEntity.class, "name", user.getName());
        if (entity == null && user.getPhone() != null)
            entity = getFirstEntityBy(UserEntity.class, "phone", user.getPhone());

        if (entity != null) {
            user.setId(entity.getId());
            user.setPhone(entity.getPhone());
            user.setName(entity.getName());
        }

        return entity;
    }
}
