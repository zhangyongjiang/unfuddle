package com.gaoshin.points.server.shard;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Properties;

import org.hibernate.shards.ShardId;
import org.hibernate.shards.id.ShardedUUIDGenerator;
import org.hibernate.shards.session.ShardedSessionImpl;

public class UuidGenerator {
    private ShardedUUIDGenerator generator = new ShardedUUIDGenerator();
    
    public String getRandomStringUuid(ShardId shardId) {
        Properties prop = new Properties();
        prop.setProperty("sharded-uuid-type", "STRING");
        generator.configure(null, prop, null);
        ShardedSessionImpl.setCurrentSubgraphShardId(shardId);
        Serializable id = generator.generate(null, null);
        return id.toString();
    }
    
    public BigInteger getRandomIntegerUuid(ShardId shardId) {
        Properties prop = new Properties();
        prop.setProperty("sharded-uuid-type", "INTEGER");
        generator.configure(null, prop, null);
        ShardedSessionImpl.setCurrentSubgraphShardId(shardId);
        Serializable id = generator.generate(null, null);
        return new BigInteger(id.toString());
    }
    
    public ShardId extractShardId(BigInteger id) {
        Properties prop = new Properties();
        prop.setProperty("sharded-uuid-type", "INTEGER");
        generator.configure(null, prop, null);
        ShardId shardId = generator.extractShardId(id);
        return shardId;
    }
    
    public ShardId extractShardId(String id) {
        Properties prop = new Properties();
        prop.setProperty("sharded-uuid-type", "STRING");
        generator.configure(null, prop, null);
        ShardId shardId = generator.extractShardId(id);
        return shardId;
    }
    
    public String sameShardRandomStringUuid(String uuid) {
        return getRandomStringUuid(extractShardId(uuid));
    }
    
    public BigInteger sameShardRandomIntegerUuid(String uuid) {
        return getRandomIntegerUuid(extractShardId(uuid));
    }
    
    public String sameShardRandomStringUuid(BigInteger uuid) {
        return getRandomStringUuid(extractShardId(uuid));
    }
    
    public BigInteger sameShardRandomIntegerUuid(BigInteger uuid) {
        return getRandomIntegerUuid(extractShardId(uuid));
    }
    
    public static void main(String[] args) throws Exception {
        UuidGenerator uuidGenerator = new UuidGenerator();
        String uuid = "000c402880db32a80132a822bcee0000";
        System.out.println(uuidGenerator.extractShardId(uuid));
        // uuidGenerator.batchIds();
    }
    
    private void batchIds() throws Exception {
        FileOutputStream fos = new FileOutputStream("uuids_decimal.txt");
        String line = "shard_id\tuuid_decimal\n";
        fos.write(line.getBytes());
        for(int i=0; i<1024; i++) {
            ShardId shardId = new ShardId(i);
            for(int j=0; j<1000; j++) {
                line = i + "\t" + getRandomIntegerUuid(shardId) + "\n";
                fos.write(line.getBytes());
            }
        }
        fos.close();
        
        fos = new FileOutputStream("uuids_string.txt");
        line = "shard_id\tuuid_string\n";
        fos.write(line.getBytes());
        for(int i=0; i<1024; i++) {
            ShardId shardId = new ShardId(i);
            for(int j=0; j<1000; j++) {
                line = i + "\t" + getRandomStringUuid(shardId) + "\n";
                fos.write(line.getBytes());
            }
        }
        fos.close();
    }

    public ShardId extractShardId(Serializable id) {
        if(id instanceof String)
            return extractShardId((String)id);
        else
            return extractShardId((BigInteger)id);
    }
}
