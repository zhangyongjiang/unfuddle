package common.util.web;

import java.text.DateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XDateAdapter extends XmlAdapter<String, Date>{
	private static final DateFormat sdf = common.util.DateUtil.getDateFormat("yyyy-MM-dd");

	@Override
	public String marshal(Date arg0) throws Exception {
		return sdf.format(arg0);
	}

	@Override
	public Date unmarshal(String arg0) throws Exception {
		return sdf.parse(arg0);
	}

}
