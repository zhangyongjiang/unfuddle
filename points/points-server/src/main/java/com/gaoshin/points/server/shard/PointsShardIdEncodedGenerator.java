package com.gaoshin.points.server.shard;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.shards.ShardId;
import org.hibernate.shards.id.ShardEncodingIdentifierGenerator;

public abstract class PointsShardIdEncodedGenerator<T> implements ShardEncodingIdentifierGenerator {
    private UuidGenerator uuidGenerator = new UuidGenerator();

    @Override
    public ShardId extractShardId(Serializable id) {
        return uuidGenerator.extractShardId(id);
    }

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return generateId(session, (T)object);
    }
    
    public abstract Serializable generateId(SessionImplementor session, T object) throws HibernateException;

}

