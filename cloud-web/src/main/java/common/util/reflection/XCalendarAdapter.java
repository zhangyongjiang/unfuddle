/**
 * Copyright (c) 2011 SORMA
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Author: sorma@gaoshin.com
 */
package common.util.reflection;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

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
