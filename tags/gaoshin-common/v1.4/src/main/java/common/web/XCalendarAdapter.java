package common.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XCalendarAdapter extends XmlAdapter<String, Calendar>{
	private static final SimpleDateFormat sdf = common.util.DateUtil.getIso8601DateFormat();

	@Override
	public String marshal(Calendar arg0) throws Exception {
		return sdf.format(arg0.getTime());
	}

	@Override
	public Calendar unmarshal(String arg0) throws Exception {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTime(sdf.parse(arg0));
		return cal;
	}

}
