package common.util.web;

import java.util.HashMap;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.core.util.Base64;
import common.util.MD5;



public class JerseyBaseResource {
	
	static final Logger logger = Logger.getLogger(JerseyBaseResource.class);
    
	protected HttpContext hc;
	protected ThreadLocal<HttpServletRequest> requestInvoker;
	protected ThreadLocal<HttpServletResponse> responseInvoker;
	
	@Context protected ResourceContext resourceContext;
  
	@Context
	public void setHttpContext(HttpContext ht) {
		this.hc = ht;
	}
	
	@Context
	public void setRequestInvoker(ThreadLocal<HttpServletRequest> requestInvoker) {
		this.requestInvoker = requestInvoker;
		checkCookie();
	}
	
	@Context
	public void setResponseInvoker(ThreadLocal<HttpServletResponse> responseInvoker) {
		this.responseInvoker = responseInvoker;
		checkCookie();
	}
	
	private void checkCookie() {
		if(requestInvoker==null || requestInvoker.get()==null || responseInvoker == null || responseInvoker.get()==null) { 
			return;
		}
	}
	
	protected void setCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(1000000);
		responseInvoker.get().addCookie(cookie);
	}

	protected String getCookie(String name) {
		return getCookieMap().get(name);
	}
	
	protected TimeZone getClientTimezZone(TimeZone timeZone) {
		String tz = getCookie("timezone");
		return tz == null ? timeZone : TimeZone.getTimeZone(tz);
	}

	private HashMap<String, String> getCookieMap() {
		Cookie[] cookies = requestInvoker.get().getCookies();
		HashMap<String, String> cookieMap = new HashMap<String, String>();
		if (cookies != null) {
			for (Cookie c : cookies) {
				cookieMap.put(c.getName(), c.getValue());
			}
		}
		
		logger.debug("cookiemap:"+cookieMap);
		
		return cookieMap;		
	}

	protected void setSessionAttribute(String name, Object value) {
		requestInvoker.get().getSession().setAttribute(name, value);
	}

	protected Object getSessionAttribute(String name) {
		return requestInvoker.get().getSession().getAttribute(name);
	}

	private void saveToAppContext(String key, Object value) {
		String addr = requestInvoker.get().getRemoteAddr();
		requestInvoker.get().getSession().getServletContext().setAttribute(addr + "." + key, value);
	}

	private Object getFromAppContext(String key) {
		try {
			String addr = requestInvoker.get().getRemoteAddr();
			return requestInvoker.get().getSession().getServletContext().getAttribute(addr + "." + key);
		} catch (Exception e) {
			return null;
		}
	}

	protected Long getRequesterUserId() {
		String cookie = getCookie("u");
		//return Long.parseLong(cookie);
	    String ori;	
		String ip = requestInvoker.get().getRemoteAddr();
		
		try {
			ori=Security.getOriginalData(cookie, ip);
			return Long.parseLong(ori);
		} catch (Exception e) {
			return null;
		}
		
	}
	
	protected String getRequesterPhoneNumber() {
		String cookie = getCookie("p");
		String ip = requestInvoker.get().getRemoteAddr();
		String phoneNumber = Security.getOriginalData(cookie, ip);
		//String phoneNumber=cookie;
		return phoneNumber;
	}
	
	protected void setUserIdCookie(String userId) {
		String cookie = Security.getVerifiableString(userId, requestInvoker.get().getRemoteAddr());
		//String cookie=userId;
		setCookie("u", cookie);
	}
	
	protected void setPhoneNumberCookie(String phone) {
		String cookie = Security.getVerifiableString(phone, requestInvoker.get().getRemoteAddr());
		//String cookie=phone;
		setCookie("p", cookie);
	}
	
	static class Security {
		private static String getVerifiableString(String data, String key) {
			String str64 = new String(Base64.encode(data));
			if(str64.endsWith("="))
				str64 = str64.substring(0, str64.length()-1) + "%";
			
			String md5 = MD5.md5(data + "-_" + key);
			String ret = str64 + "-" + md5;
			return ret;
		}
		
		private static String getOriginalData(String data, String key) {
			if(data == null) {
				return null;
			}
			
			//if(true)
			//	return data;

			int pos = data.indexOf("-");
			if(pos == -1) {
				return null;
			}
			
			String str64 = data.substring(0, pos);
			if(str64.endsWith("%")) 
				str64 = str64.substring(0, str64.length()-1) + "=";
			String validation = data.substring(pos + 1);
			String result = new String(Base64.decode(str64.getBytes())).trim();
			
			String md5 = MD5.md5(result + "-_" + key);
			if(!md5.equals(validation)) {
				return null;
			}
			
			return result;
		}
	}

    protected <T> T getResource(Class<T> resourceClass) {
        return resourceContext.getResource(resourceClass);
    }
}
