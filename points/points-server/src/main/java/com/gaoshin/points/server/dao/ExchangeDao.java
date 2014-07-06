package com.gaoshin.points.server.dao;

import com.gaoshin.points.server.entity.UserBalanceEntity;

public interface ExchangeDao extends GenericDao {
	UserBalanceEntity getUserBalance(String userId, String itemId);
	void adjustBalance(String userId, String itemId, int points);
}
