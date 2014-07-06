package common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
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

	public static void main(String[] args) {
		System.out.println(getIso8601DateFormat().format(new Date()));
	}
}
