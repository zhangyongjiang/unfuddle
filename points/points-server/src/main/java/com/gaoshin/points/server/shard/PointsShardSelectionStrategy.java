package com.gaoshin.points.server.shard;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.shards.ShardId;
import org.hibernate.shards.strategy.selection.ShardSelectionStrategy;

import com.gaoshin.points.server.entity.UserBalanceEntity;
import com.gaoshin.points.server.entity.UserEntity;
import com.gaoshin.points.server.entity.ExchangeHistoryEntity;
import common.util.reflection.ReflectionUtil;

public class PointsShardSelectionStrategy implements ShardSelectionStrategy {
    private int maxVirtualShards;
    private Map<Class, ShardSelector> shardSelectors;
    private UuidGenerator uuidGenerator = new UuidGenerator();

    public PointsShardSelectionStrategy(int numOfShards) {
        this.maxVirtualShards = numOfShards;
        shardSelectors = new HashMap<Class, ShardSelector>();
        shardSelectors.put(UserEntity.class, new UserEntityShardSelector());
        shardSelectors.put(UserBalanceEntity.class, new SameUserShardSelector());
        shardSelectors.put(ExchangeHistoryEntity.class, new SameUserShardSelector());
    }
    
    @Override
    public ShardId selectShardIdForNewObject(Object obj) {
        ShardSelector shardSelector = shardSelectors.get(obj.getClass());
        if(shardSelector == null) {
        	return new ShardId(0);
        }
        else {
        	return shardSelector.getShard(obj);
        }
    }
    
    private class UserEntityShardSelector implements ShardSelector<UserEntity> {
        @Override
        public ShardId getShard(UserEntity obj) {
            int shardId = obj.getCityId() % maxVirtualShards;
            return new ShardId(shardId);
        }
    }

    private class SameUserShardSelector implements ShardSelector<Object> {
    	private String fieldName = "userId";
    	
        @Override
        public ShardId getShard(Object obj) {
        	try {
				Field field = ReflectionUtil.getField(obj.getClass(), fieldName);
				String userId = (String) field.get(obj);
				return uuidGenerator.extractShardId(userId);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
        }
    }
    
}
