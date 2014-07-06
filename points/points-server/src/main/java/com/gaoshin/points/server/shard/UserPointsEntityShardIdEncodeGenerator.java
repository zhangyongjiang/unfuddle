package com.gaoshin.points.server.shard;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;

import com.gaoshin.points.server.entity.UserBalanceEntity;

public class UserPointsEntityShardIdEncodeGenerator extends PointsShardIdEncodedGenerator<UserBalanceEntity> {
    @Override
    public Serializable generateId(SessionImplementor session, UserBalanceEntity object) throws HibernateException {
        return object.getId();
    }
}
