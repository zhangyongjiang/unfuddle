package com.gaoshin.points.server.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.gaoshin.points.server.dao.ExchangeDao;
import com.gaoshin.points.server.entity.UserBalanceEntity;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

@Repository("exchangeDao")
public class ExchangeDaoImpl extends HibernateBaseDao implements ExchangeDao {

	@Override
	public void adjustBalance(String userId, String itemId, int points) {
		UserBalanceEntity balance = getUserBalance(userId, itemId);
		if(balance == null) {
			balance = new UserBalanceEntity();
			balance.setPoints(points);
			balance.setUserId(userId);
			balance.setItemId(itemId);
			if(balance.getPoints() < 0) {
				throw new BusinessException(ServiceError.NoEnoughBalance);
			}
			insertEntity(balance);
		} 
		else {
			balance.setPoints(balance.getPoints() + points);
			if(balance.getPoints() < 0) {
				throw new BusinessException(ServiceError.NoEnoughBalance);
			}
			saveEntity(balance);
		}
	}

	@Override
	public UserBalanceEntity getUserBalance(String userId, String itemId) {
		return findUnique(UserBalanceEntity.class, "from UserBalanceEntity ube where ube.userId=? and ube.itemId=?", userId, itemId);
	}
}
