package com.gaoshin.cloud.web.job.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.gaoshin.cloud.web.job.bean.JobConfKey;

public class Parameter  {
    public static Map<String, String> replace(Map<String, String> params) {
        Map<String, String> basic = new HashMap<String, String>();
        setTimestamp(params);
        while(true) {
            int size1 = params.size();
            int size2 = basic.size();
            String[] keys = params.keySet().toArray(new String[0]);
            for(String key : keys) {
                String value = params.get(key);
                if(value.indexOf("${")==-1) {
                    basic.put(key, value);
                    params.remove(key);
                    continue;
                }
            }
            
            keys = params.keySet().toArray(new String[0]);
            for(String key : keys) {
                String value = params.get(key);
                for(String search : basic.keySet()) {
                    String replaceValue = basic.get(search);
                    value = value.replaceAll("\\$\\{" + search + "\\}", replaceValue);
                    if(value.indexOf("${")==-1) {
                        break;
                    }
                }
                if(value.indexOf("${")==-1) {
                    basic.put(key, value);
                    params.remove(key);
                }
                else {
                    params.put(key, value);
                }
            }
            if(size1 == params.size() && size2 == basic.size()) {
                break;
            }
        }
        
        return basic;
    }

    private static void setTimestamp(Map<String, String> params) {
        String timestamp = params.get(JobConfKey.Timestamp.name());
        if(timestamp == null) {
            return;
        }
        long ts = Long.parseLong(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date(ts);
        String datestr = sdf.format(date);
        String[] times = datestr.split("-");
        params.put("year", times[0]);
        params.put("month", times[1]);
        params.put("day", times[2]);
        params.put("hour", times[3]);
        params.put("minute", times[4]);
        params.put("second", times[5]);
    }
}
