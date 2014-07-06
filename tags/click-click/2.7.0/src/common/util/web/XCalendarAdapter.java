package common.util.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import common.util.reflection.ReflectionUtil;

public class XCalendarAdapter {
	private static final SimpleDateFormat sdf = ReflectionUtil.getIso8601DateFormat();

	public static String marshal(Calendar arg0) throws Exception {
		return sdf.format(arg0.getTime());
	}

	public static Calendar unmarshal(String arg0) throws Exception {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTime(sdf.parse(arg0));
		return cal;
	}
}
