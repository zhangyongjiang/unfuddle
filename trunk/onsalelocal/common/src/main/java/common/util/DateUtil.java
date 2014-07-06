package common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DateUtil {
    public static long DAY = 86400000l;
    public static int DAY_SECOND = 86400;
    public static int DAYS_30 = 2592000;
    public static int ONE_HOUR = 3600;
    public static Map<String, Integer> months = new HashMap<String, Integer>(){{
        put("JANUARY", 1);
        put("JAN", 1);
        put("FEBRUARY", 2);
        put("FEB", 2);
        put("MARCH", 3);
        put("MAR", 3);
        put("APRIL", 4);
        put("APR", 4);
        put("MAY", 5);
        put("MAY", 5);
        put("JUNE", 6);
        put("JUN", 6);
        put("JULY", 7);
        put("JUL", 7);
        put("AUGUST", 8);
        put("AUG", 8);
        put("SEPTEMBER", 9);
        put("SEP", 9);
        put("SEPT", 9);
        put("OCTOBER", 10);
        put("OCT", 10);
        put("NOVEMBER", 11);
        put("NOV", 11);
        put("DECEMBER", 12);
        put("DEC", 12);
    }};
    
	public static SimpleDateFormat getIso8601DateFormat() {
		final SimpleDateFormat ISO8601UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");// 24
		// characters
		ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC == GMT
		return ISO8601UTC;
	}
	
	public static DateFormat getDateFormat(String format) {
		return new SimpleDateFormat(format);
	}
	
	public static Calendar getCurrentUtcTime() {
		return Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	}

	public static Long parse(String s) {
	    if(s == null)
	        return null;
	    s = s.toUpperCase().trim();
	    if(s.length() == 0)
	        return null;
	    s.replaceAll("[-/ \\.]+", " ");
	    String[] item = s.split(" ");
	    
	    Integer year = null;
        try {
            year = Integer.parseInt(item[0]);
        }
        catch (Exception e) {
        }
        
        Integer month = null;
        Integer day = null;
        if(year != null) {
            try {
                month = Integer.parseInt(item[1]);
            }
            catch (Exception e) {
                month = months.get(item[1]);
            }
            if(month == null)
                throw new RuntimeException("unknow date format");
        }
        
        return null;
	}
}
