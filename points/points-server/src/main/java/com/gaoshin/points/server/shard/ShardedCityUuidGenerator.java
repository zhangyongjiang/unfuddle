package com.gaoshin.points.server.shard;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.shards.ShardId;
import org.hibernate.shards.id.ShardEncodingIdentifierGenerator;

import common.util.reflection.ReflectionUtil;

public class ShardedCityUuidGenerator implements ShardEncodingIdentifierGenerator {
    private UuidGenerator uuidGenerator = new UuidGenerator();

    @Override
    public ShardId extractShardId(Serializable id) {
        return uuidGenerator.extractShardId(id);
    }

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
    	try {
			Class cls = object.getClass();
			Field field = ReflectionUtil.getField(cls, "cityId");
			field.setAccessible(true);
			int cityId = (Integer) field.get(object);
			return uuidGenerator.getRandomStringUuid(new ShardId(cityId));
		} catch (Exception e) {
			throw new HibernateException(e);
		}
    }
}

