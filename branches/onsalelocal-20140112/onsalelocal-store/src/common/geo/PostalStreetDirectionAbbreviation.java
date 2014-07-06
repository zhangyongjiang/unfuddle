package common.geo;

import java.util.HashMap;
import java.util.Map;

public class PostalStreetDirectionAbbreviation {
    public static final Map<String, String> long2Short = new HashMap<String, String>() {
        {
            put("NORTH", "N");
            put("EAST", "E");
            put("SOUTH", "S");
            put("WEST", "W");
            put("NORTHEAST", "NE");
            put("SOUTHEAST", "SE");
            put("NORTHWEST", "NW");
            put("SOUTHWEST", "SW");
        }
    };
    
    public static final Map<String, String> short2Long = new HashMap<String, String>() {
        {
            for(Map.Entry<String, String> entry : long2Short.entrySet()) {
                put(entry.getValue(), entry.getKey());
            }
        }
    };
}
