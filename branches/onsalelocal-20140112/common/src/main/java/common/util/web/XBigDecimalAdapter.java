package common.util.web;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XBigDecimalAdapter extends XmlAdapter<String, BigDecimal>{
	@Override
	public String marshal(BigDecimal arg0) throws Exception {
		return arg0.doubleValue() + "";
	}

	@Override
	public BigDecimal unmarshal(String arg0) throws Exception {
		return BigDecimal.valueOf(Double.parseDouble(arg0));
	}

}
