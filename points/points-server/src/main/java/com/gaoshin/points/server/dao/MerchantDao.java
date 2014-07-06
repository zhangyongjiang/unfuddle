package com.gaoshin.points.server.dao;

import com.gaoshin.points.server.entity.CashierEntity;

public interface MerchantDao extends GenericDao {

	CashierEntity findByCashierMerchantId(String cashierId);
}
