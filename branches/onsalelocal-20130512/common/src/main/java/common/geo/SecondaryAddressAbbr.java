package common.geo;

import java.util.HashMap;
import java.util.Map;

public class SecondaryAddressAbbr {
    public static final Map<String, String> long2Short = new HashMap<String, String>() {
        {
            put("APARTMENT", "APT");
            put("BASEMENT", "BSMT");
            put("BUILDING", "BLDG");
            put("DEPARTMENT", "DEPT");
            put("FLOOR", "FL");
            put("FRONT", "FRNT");
            put("HANGAR", "HNGR");
            put("LOBBY", "LBBY");
            put("LOT", "LOT");
            put("LOWER", "LOWR");
            put("OFFICE", "OFC");
            put("PENTHOUSE", "PH");
            put("PIER", "PIER");
            put("REAR", "REAR");
            put("ROOM", "RM");
            put("SIDE", "SIDE");
            put("SLIP", "SLIP");
            put("SPACE", "SPC");
            put("STOP", "STOP");
            put("SUITE", "STE");
            put("TRAILER", "TRLR");
            put("UNIT", "UNIT");
            put("UPPER", "UPPR");
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
