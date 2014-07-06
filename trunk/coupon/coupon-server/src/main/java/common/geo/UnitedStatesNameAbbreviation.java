package common.geo;

import java.util.HashMap;
import java.util.Map;

public class UnitedStatesNameAbbreviation {
    public static final Map<String, String> long2Short = new HashMap<String, String>() {
        {
            put("ALABAMA", "AL");
            put("ALASKA", "AK");
            put("ARIZONA", "AZ");
            put("ARKANSAS", "AR");
            put("CALIFORNIA", "CA");
            put("COLORADO", "CO");
            put("CONNECTICUT", "CT");
            put("DELAWARE", "DE");
            put("FLORIDA", "FL");
            put("GEORGIA", "GA");
            put("HAWAII", "HI");
            put("IDAHO", "ID");
            put("ILLINOIS", "IL");
            put("INDIANA", "IN");
            put("IOWA", "IA");
            put("KANSAS", "KS");
            put("KENTUCKY", "KY");
            put("LOUISIANA", "LA");
            put("MAINE", "ME");
            put("MARYLAND", "MD");
            put("MASSACHUSETTS", "MA");
            put("MICHIGAN", "MI");
            put("MINNESOTA", "MN");
            put("MISSISSIPPI", "MS");
            put("MISSOURI", "MO");
            put("MONTANA", "MT");
            put("NEBRASKA", "NE");
            put("NEVADA", "NV");
            put("NEW HAMPSHIRE", "NH");
            put("NEW JERSEY", "NJ");
            put("NEW MEXICO", "NM");
            put("NEW YORK", "NY");
            put("NORTH CAROLINA", "NC");
            put("NORTH DAKOTA", "ND");
            put("OHIO", "OH");
            put("OKLAHOMA", "OK");
            put("OREGON", "OR");
            put("PENNSYLVANIA", "PA");
            put("RHODE ISLAND", "RI");
            put("SOUTH CAROLINA", "SC");
            put("SOUTH DAKOTA", "SD");
            put("TENNESSEE", "TN");
            put("TEXAS", "TX");
            put("UTAH", "UT");
            put("VERMONT", "VT");
            put("VIRGINIA", "VA");
            put("WASHINGTON", "WA");
            put("WEST VIRGINIA", "WV");
            put("WISCONSIN", "WI");
            put("WYOMING", "WY");
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
