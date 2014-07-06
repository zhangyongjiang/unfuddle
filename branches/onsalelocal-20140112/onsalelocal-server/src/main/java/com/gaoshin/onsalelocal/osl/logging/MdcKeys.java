package com.gaoshin.onsalelocal.osl.logging;

import java.util.HashSet;


public enum MdcKeys {
    Hostname,
    LoggerName,
    StartTime,
    TimeSince,
    TimeDiff,
    AppVer,
    DeviceId,
    DeviceType,
    ClientRequestTime,
    Uri,
    Query,
    UserId,
    PlaceId,
    Reqid,
    Method,
    Status,
    PathParam, 
    LogTime, 
    Bytes, 
    Latitude, 
    Longitude;
    
    private static HashSet<String> keysAdded = new HashSet<String>();
    
    public synchronized static void addKey(String key) {
        keysAdded.add(key);
    }
    
    public static String[] getAllKeys() {
        return keysAdded.toArray(new String[0]);
    }
}
