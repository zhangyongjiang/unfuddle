package com.gaoshin.points.server.shard;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.shards.ShardId;
import org.hibernate.shards.id.ShardEncodingIdentifierGenerator;

import common.util.reflection.ReflectionUtil;

public class SameUserShardIdGenerator implements ShardEncodingIdentifierGenerator {
    private UuidGenerator uuidGenerator = new UuidGenerator();

    @Override
    public ShardId extractShardId(Serializable id) {
        return uuidGenerator.extractShardId(id);
    }

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
    	try {
			Class cls = object.getClass();
			Field field = ReflectionUtil.getField(cls, "userId");
			field.setAccessible(true);
			String userId = (String) field.get(object);
			return uuidGenerator.sameShardRandomStringUuid(userId);
		} catch (Exception e) {
			throw new HibernateException(e);
		}
    }
}

