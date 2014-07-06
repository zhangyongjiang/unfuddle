package com.gaoshin.points.server.shard;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;

import com.gaoshin.points.server.entity.CashierEntity;

public class CashierIdGenerator extends PointsShardIdEncodedGenerator<CashierEntity> {
	private UuidGenerator generator = new UuidGenerator();

	@Override
	public Serializable generateId(SessionImplementor session, CashierEntity object)
			throws HibernateException {
		String merchantId = object.getMerchantId();
		return generator.sameShardRandomStringUuid(merchantId);
	}

}
