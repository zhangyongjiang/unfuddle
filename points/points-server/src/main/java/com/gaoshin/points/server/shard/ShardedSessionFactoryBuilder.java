package com.gaoshin.points.server.shard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.shards.ShardId;
import org.hibernate.shards.ShardedConfiguration;
import org.hibernate.shards.cfg.ConfigurationToShardConfigurationAdapter;
import org.hibernate.shards.cfg.ShardConfiguration;
import org.hibernate.shards.strategy.ShardStrategy;
import org.hibernate.shards.strategy.ShardStrategyFactory;
import org.hibernate.shards.strategy.ShardStrategyImpl;
import org.hibernate.shards.strategy.access.SequentialShardAccessStrategy;
import org.hibernate.shards.strategy.access.ShardAccessStrategy;
import org.hibernate.shards.strategy.resolution.AllShardsShardResolutionStrategy;
import org.hibernate.shards.strategy.resolution.ShardResolutionStrategy;
import org.hibernate.shards.strategy.selection.ShardSelectionStrategy;

import com.gaoshin.points.server.entity.CashierEntity;
import com.gaoshin.points.server.entity.ExchangeHistoryEntity;
import com.gaoshin.points.server.entity.ItemEntity;
import com.gaoshin.points.server.entity.TransactionEntity;
import com.gaoshin.points.server.entity.UserBalanceEntity;
import com.gaoshin.points.server.entity.UserEntity;

public class ShardedSessionFactoryBuilder {
    public final static int maxVirtualShards = 4096;
    
    private int numOfShards;
    private String shardConfPath;

    public ShardedSessionFactoryBuilder() {
    }
    
    public SessionFactory createSessionFactory() {
        AnnotationConfiguration prototypeConfig = new AnnotationConfiguration();
        prototypeConfig.configure(getHibernateShardConf(0));
        prototypeConfig.addAnnotatedClass(UserEntity.class);
        prototypeConfig.addAnnotatedClass(ItemEntity.class);
        prototypeConfig.addAnnotatedClass(TransactionEntity.class);
        prototypeConfig.addAnnotatedClass(UserBalanceEntity.class);
        prototypeConfig.addAnnotatedClass(ExchangeHistoryEntity.class);
        prototypeConfig.addAnnotatedClass(CashierEntity.class);

        List<ShardConfiguration> shardConfigs = new ArrayList<ShardConfiguration>();
        for (int i=0; i<numOfShards; i++) {
            shardConfigs.add(buildShardConfig(getHibernateShardConf(i)));
        }

        Map<Integer, Integer> virtualShardMap = new HashMap<Integer, Integer>();
        for(int i=0; i<maxVirtualShards; i++) {
            virtualShardMap.put(i, i / (maxVirtualShards/numOfShards));
        }
        
        ShardStrategyFactory shardStrategyFactory = buildShardStrategyFactory();
        ShardedConfiguration shardedConfig = new ShardedConfiguration(
                prototypeConfig,
                shardConfigs,
                shardStrategyFactory, virtualShardMap);
        return shardedConfig.buildShardedSessionFactory();
    }

    private Configuration getPrototypeConfig(String hibernateFile, List<String> resourceFiles) {
        Configuration prototypeConfig = this.getConfiguration(hibernateFile);
        for (String res : resourceFiles) {
            prototypeConfig.addResource(res);
        }
        return prototypeConfig;
    }

    private Configuration getConfiguration(String file) {
        return new Configuration().configure(file);
    }
    
    private ShardStrategyFactory buildShardStrategyFactory() {
        ShardStrategyFactory shardStrategyFactory = new ShardStrategyFactory() {
            public ShardStrategy newShardStrategy(List<ShardId> shardIds) {
                ShardSelectionStrategy pss = new PointsShardSelectionStrategy(maxVirtualShards);
                ShardResolutionStrategy prs = new AllShardsShardResolutionStrategy(shardIds);
                ShardAccessStrategy pas = new SequentialShardAccessStrategy();
                return new ShardStrategyImpl(pss, prs, pas);
            }
        };
        return shardStrategyFactory;
    }

    private ShardConfiguration buildShardConfig(String configFile) {
        Configuration config = new Configuration().configure(configFile);
        return new ConfigurationToShardConfigurationAdapter(config);
    }

    public int getNumOfShards() {
        return numOfShards;
    }

    public void setNumOfShards(int numOfShards) {
        this.numOfShards = numOfShards;
    }

    public String getHibernateShardConf(int index) {
        return getShardConfPath() + "." + index;
    }

    public String getShardConfPath() {
        return shardConfPath;
    }

    public void setShardConfPath(String shardConfPath) {
        this.shardConfPath = shardConfPath;
    }

}
