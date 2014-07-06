package com.gaoshin.onsalelocal.osl.logging;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.MDC;

public class TimeDiff {
    private Map<String, Long> lastLogTimeMap = new HashMap<String, Long>();
    
    @Override
    public String toString() {
        String logger = (String) MDC.get(MdcKeys.LoggerName.name());
        String key = logger + Thread.currentThread().getName();
        Long lastLogTime = lastLogTimeMap.get(key);
        Long now = System.currentTimeMillis();
        if(lastLogTime == null)
            lastLogTime = now;
        String log = String.valueOf(now - lastLogTime);
        lastLogTimeMap.put(key, now);
        
        return log;
    }
}
