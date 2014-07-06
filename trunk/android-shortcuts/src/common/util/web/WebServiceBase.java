package common.util.web;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

public class WebServiceBase {
	
	private DefaultHttpClient httpClient = null;
	private String baseUrl = null;

	public WebServiceBase(DefaultHttpClient httpClient, String baseUrl) {
		this.httpClient  = httpClient;
		this.baseUrl = baseUrl;
	}
	
	public String getFullPath(String path) {
		return baseUrl + path;
	}

	public <T> T post(String path, Class<T> resultType, Object request) throws Exception {
		return WebServiceUtil.post(getFullPath(path), httpClient, resultType, request);
	}

	public <T> T get(String path, Class<T> resultType) throws ClientProtocolException, ServiceException, IOException {
		return WebServiceUtil.get(httpClient, getFullPath(path), resultType, "application/json");
	}
	
	public String getCookie(String name) {
		CookieStore cookieStore = httpClient.getCookieStore();
		if(cookieStore == null)
			return null;
		
		List<Cookie> cookies = cookieStore.getCookies();
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals(name)) {
				return cookie.getValue();
			}
		}
		return null;
	}
	
	public String getDomain() {
        return WebServiceUtil.getDomain(baseUrl);
	}
	
	public int getPort() {
		String[] split = baseUrl.split("/");
		String domainPlusPort = split[2];
		if(domainPlusPort.indexOf(":") == -1)
			return 80;
		else
			return Integer.parseInt(domainPlusPort.split(":")[1]);
	}
	
	public void addCookie(String name, String value) {
        WebServiceUtil.addCookie(httpClient, name, value, getDomain());
	}
	
	public DefaultHttpClient getHttpClient() {
		return httpClient;
	}
}
