package com.gaoshin.points.server.shard;

import org.hibernate.shards.ShardId;

public interface ShardSelector<T> {
    ShardId getShard(T obj);
}
