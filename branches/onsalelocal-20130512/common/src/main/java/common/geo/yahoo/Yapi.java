package common.geo.yahoo;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Yapi {
	public static final String KEY = "aeNsXqDV34FWzgWjUFFeckfQRKsStWgTz5e9Rs3aSwfbIQxVDZzDY9ihJH6FXL3p7nYfkik";
	private static JAXBContext ctx;
	static {
		try {
			ctx = JAXBContext.newInstance(Yresponse.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static Yresponse geo(String location) throws Exception {
		String url = "http://where.yahooapis.com/geocode?q=" + URLEncoder.encode(location, "UTF-8") + "&appid=" + KEY;
		System.out.println(url);
		InputStream stream = new URL(url).openStream();
		Unmarshaller unmarshaller = ctx.createUnmarshaller();
		Yresponse resp = (Yresponse) unmarshaller.unmarshal(stream);
		return resp;
	}
	
	public static void main(String[] args) throws Exception {
		Yresponse resp = geo("4701 Sangamore Rd. Bethesda, MD 20816");
		System.out.println(resp.getResult().getLatitude() + ", " + resp.getResult().getLongitude());
	}
}
