package common.util.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import common.util.reflection.ReflectionUtil;

public class XDateAdapter {
	private static final SimpleDateFormat sdf = ReflectionUtil.getIso8601DateFormat();

	public static String marshal(Date arg0) throws Exception {
		return sdf.format(arg0);
	}

	public static Date unmarshal(String arg0) throws Exception {
		return sdf.parse(arg0);
	}
}
