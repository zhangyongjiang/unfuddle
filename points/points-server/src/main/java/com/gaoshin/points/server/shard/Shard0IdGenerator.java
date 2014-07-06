package com.gaoshin.points.server.shard;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.shards.ShardId;

public class Shard0IdGenerator implements IdentifierGenerator {
	private UuidGenerator generator;
	
	public Shard0IdGenerator() {
		generator = new UuidGenerator();
	}

	@Override
	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException {
		return generator.getRandomStringUuid(new ShardId(0));
	}

}
