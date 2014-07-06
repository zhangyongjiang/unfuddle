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
import common.util.DesEncrypter;
import common.util.MD5;

public class JerseyBaseResource {
    public static final String UserCookieName = "ucn";
	private static final Logger logger = Logger.getLogger(JerseyBaseResource.class);
	
	@Context protected HttpContext hc;
	@Context protected ThreadLocal<HttpServletRequest> requestInvoker;
	@Context protected ThreadLocal<HttpServletResponse> responseInvoker;
	@Context protected ResourceContext resourceContext;
  
	public String getRemoteIp(){
		return requestInvoker.get().getRemoteAddr();
	}
	
	protected void removeCookie(String name) {
        logger.debug(getRemoteIp() + " removeCookie " + name);
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        responseInvoker.get().addCookie(cookie);
	}
	
	protected void setCookie(String name, String value) {
	    logger.debug(getRemoteIp() + " setCookie " + name + "=" + value);
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
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
		
		logger.debug(getRemoteIp() + " cookiemap: "+cookieMap);
		if(!cookieMap.containsKey("fct")) {
		    setCookie("fct", String.valueOf(System.currentTimeMillis()));
		}
		
		return cookieMap;		
	}

	protected String assertRequesterUserId() {
	    String userId = getRequesterUserId();
	    if(userId == null) {
	        System.out.println(ServiceError.NoGuest + ", Cookie: " + requestInvoker.get().getHeader("Cookie"));
	        throw new BusinessException(ServiceError.NoGuest, requestInvoker.get().getHeader("Cookie"));
	    }
	    return userId;
	}
	
    protected String getRequestUserIdFromHeader() {
        String userId = requestInvoker.get().getHeader(UserCookieName);;
        return userId;
    }

    protected String getRequestUserIdFromCookie() {
        String userId = getCookie(UserCookieName);
        if(userId == null) {
            return null;
        }
        DesEncrypter encrypter = new DesEncrypter(getRemoteIp());
        String decrypted = encrypter.decrypt(userId);
        if(decrypted != null) {
            userId = decrypted;
        }
        else {
            removeCookie(UserCookieName);
            return null;
        }
        return userId;
    }

    protected String getRequesterUserId() {
        String userId = null;
        userId = getRequestUserIdFromCookie();
        if(userId == null) {
            userId = getRequestUserIdFromHeader();
        }
        
        String syncCookie = getCookie("synced");
        if(!"1".equals(syncCookie)) {
            responseInvoker.get().setHeader("Cache-Control", "no-cache");
        }
        
        return userId;
    }
	
	protected String getPhone(String phone){
		String[] strs=phone.split("_");	
		try{
		    String encrypted = strs[1];
            String key = strs[0];
            String ori = new DesEncrypter(key.substring(0, key.length()-1)).decrypt(encrypted);
            if(ori == null) {
                ori = new DesEncrypter(key).decrypt(encrypted);
            }
            if(ori == null) {
                ori = phone;
            }
		    return ori;
		}  catch (Exception e) {
            logger.warn("cannot decrypt phone cookie " + phone);
		    return phone;
		}
			
	}
	
	protected void setUserIdCookie(String userId) {
	    DesEncrypter encrypter = new DesEncrypter(getRemoteIp());
	    String encrypted = encrypter.encrypt(userId);
	    encrypted = encrypted.replace("\r\n", "<br>");              
	    encrypted = encrypted.replace("\n", "<br>");  
		setCookie(UserCookieName, encrypted);
		logger.debug("set user cookie " + encrypted);
	}
	
	protected void removeUserCookie() {
	    removeCookie(UserCookieName);
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

    protected Float getValue(String str) {
    	if(str == null || str.trim().length() == 0)
    		return null;
    	return Float.parseFloat(str);
    }

    protected Integer getIntValue(String str) {
    	if(str == null || str.trim().length() == 0)
    		return null;
    	return Integer.parseInt(str);
    }
}
