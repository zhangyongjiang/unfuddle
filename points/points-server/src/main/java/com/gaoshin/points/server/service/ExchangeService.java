package com.gaoshin.points.server.service;

import com.gaoshin.points.server.bean.ExchangeHistory;

public interface ExchangeService {

	void setPoints(String superUserId, String itemId, int points);

	ExchangeHistory tradePoints(String userId, ExchangeHistory trade);

}
