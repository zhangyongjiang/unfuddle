package com.gaoshin.points.server.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.gaoshin.points.server.dao.MerchantDao;
import com.gaoshin.points.server.entity.CashierEntity;

@Repository("merchantDao")
public class MerchantDaoHibernateImpl extends HibernateBaseDao implements MerchantDao {

	@Override
	public CashierEntity findByCashierMerchantId(String cashierId) {
		return findUnique(CashierEntity.class, "from CashierEntity ce where ce.cashierMerchantId=?", cashierId);
	}
}
