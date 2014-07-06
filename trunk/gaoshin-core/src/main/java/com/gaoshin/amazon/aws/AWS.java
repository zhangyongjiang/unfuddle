package com.gaoshin.amazon.aws;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class AWS {

	private static String appid = "1T1HRK7KA46TMYXP9GR2";

	private static long lastAccessTime = 0l;
	private static Object lock = new Object();

	public AWS() {
	}

	public static String getAppid() {
		return appid;
	}

	public static void setAppid(String appid) {
		AWS.appid = appid;
	}

	protected static void sync() {
		synchronized (lock) {
			while (true) {
				long now = new Date().getTime();
                if ((now - lastAccessTime) < 2000) {
					try {
                        Thread.sleep(2000 + lastAccessTime - now);
					} catch (InterruptedException e) {
					}
				} else {
					lastAccessTime = now;
					break;
				}
			}
		}
	}

	public static String getRequestUrl(Object req) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
		HashMap<String, String> paras = new HashMap<String, String>();
        paras.put("Version", "2010-09-01");
		paras.put("Service", "AWSECommerceService");

		String className = req.getClass().getSimpleName();
		String operation = className.substring(0, className.indexOf("Request"));
		paras.put("Operation", operation);

		Field[] fields = req.getClass().getDeclaredFields();
		for(Field field : fields) {
			XmlElement xmlElement = field.getAnnotation(XmlElement.class);
			if(xmlElement == null) {
                continue;
            }
			field.setAccessible(true);
			String name = xmlElement.name();
			Object value;
			try {
				value = field.get(req);
			} catch (Exception e) {
				continue;
			}
			if(value == null) {
                continue;
            }
			if(value instanceof List) {
				List list = (List)value;
				StringBuffer sb = new StringBuffer();
				for(int i=0; i<list.size(); i++) {
					sb.append(list.get(i));
					if(i != (list.size() - 1)) {
                        sb.append(",");
                    }
				}
				paras.put(name, sb.toString());
			}
			else {
				paras.put(name, value.toString());
			}
		}

		String url = new SignedRequestsHelper().sign(paras);
        System.out.println(url);
		return url;
	}
}
