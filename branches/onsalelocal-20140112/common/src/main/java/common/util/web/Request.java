package common.util.web;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;

import common.util.reflection.FieldFoundCallback;
import common.util.reflection.ReflectionUtil;

public class Request {
	private String uri;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String toUrl() {
		final StringBuilder sb = new StringBuilder();
		sb.append(uri).append("?");
		try {
			ReflectionUtil.iterateFields(this, new FieldFoundCallback() {
				@Override
				public void field(Object o, Field field) throws Exception {
					if(Modifier.STATIC == (Modifier.STATIC & field.getModifiers())) 
						return;
					
					field.setAccessible(true);
					if(field.getName().equals("uri"))
						return;
					Object value = field.get(o);
					if(value != null) {
						sb.append(field.getName()).append("=").append(URLEncoder.encode(value.toString(), "UTF-8")).append("&");
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
