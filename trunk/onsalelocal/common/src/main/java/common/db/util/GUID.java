package common.db.util;

import java.util.UUID;

public class GUID {
    public static String getRandomStringUuid(short shardId) {
        String fmt = String.format("%03X", shardId);
        return fmt + UUID.randomUUID().toString();
    }
    
    public static short extractShardId(String id) {
        return Short.parseShort(id.substring(0,3), 16);
    }
    
    public static String sameShardRandomStringUuid(String guid) {
        int shardId = extractShardId(guid);
        String fmt = String.format("%03X", shardId);
        return fmt + UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        short shardId = 234;
        String guid = getRandomStringUuid(shardId);
        String newGuid = sameShardRandomStringUuid(guid);
        short newShardId = extractShardId(newGuid);
        System.out.println(shardId + ", " + guid + ", " + newShardId + ", " + newGuid);
    }
}
