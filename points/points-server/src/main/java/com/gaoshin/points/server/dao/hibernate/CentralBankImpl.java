package com.gaoshin.points.server.dao.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gaoshin.points.server.dao.CentralBank;
import com.gaoshin.points.server.dao.UserDao;
import com.gaoshin.points.server.entity.ItemEntity;
import com.gaoshin.points.server.entity.ItemStatus;
import com.gaoshin.points.server.entity.UserEntity;
import com.gaoshin.points.server.entity.UserStatus;
import com.gaoshin.points.server.entity.UserType;
import com.gaoshin.points.server.shard.ShardedSessionFactoryBuilder;

@Repository("centralBank")
public class CentralBankImpl extends HibernateBaseDao implements CentralBank {
	private UserDao userDao;
	private UserEntity chairman;
	private ItemEntity money;

	public int getChairmanCity() {
		return ShardedSessionFactoryBuilder.maxVirtualShards - 1;
	}
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
		int cityId = getChairmanCity();
		chairman = userDao.findUnique(UserEntity.class, "from UserEntity ue where ue.cityId=?", cityId);
		if(chairman == null) {
			chairman = new UserEntity();
			chairman.setCityId(cityId);
			chairman.setPhone("88888888");
			chairman.setUserStatus(UserStatus.Registered);
			chairman.setUserType(UserType.Super);
			chairman.setName("Central Bank Chairman");
			chairman.setPassword("fa37275deddfadda7fd4d0a8298d117b");
			userDao.insertEntity(chairman);
		}
		
		money = userDao.findUnique(ItemEntity.class, "from ItemEntity ie where ie.userId=?", chairman.getId());
		if(money == null) {
			money = new ItemEntity();
			money.setExpire(Long.MAX_VALUE);
			money.setPoints(100);
			money.setStatus(ItemStatus.Active);
			money.setUserId(chairman.getId());
			money.setStartTime(0);
			userDao.insertEntity(money);
		}
	}

	@Override
	public UserEntity getChairman() {
		return chairman;
	}

	@Override
	public ItemEntity getMoney() {
		return money;
	}
}
