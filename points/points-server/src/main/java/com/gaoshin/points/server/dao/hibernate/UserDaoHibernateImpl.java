package com.gaoshin.points.server.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gaoshin.points.server.dao.UserDao;
import com.gaoshin.points.server.entity.UserEntity;

@Repository("userDao")
public class UserDaoHibernateImpl extends HibernateBaseDao implements UserDao {

	@Override
	public UserEntity findUserByPhone(String phone) {
		List list = hibernateTemplate.find("from UserEntity ue where ue.phone=?", phone);
		return list.isEmpty() ? null : ((UserEntity)list.get(0));
	}

}
