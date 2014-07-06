package common.util.web;


import java.util.HashMap;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.core.util.Base64;
import common.util.DesEncrypter;
import common.util.MD5;



public class JerseyBaseResource {
	static final Logger logger = Logger.getLogger(JerseyBaseResource.class);
    public static final boolean useHeader = false;
	
	@Value("${domain}")
    private String domain;
    
    private boolean encrypt = true;
    
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

	public String getRemoteIp(){
		return requestInvoker.get().getRemoteAddr();
	}
	
	private void checkCookie() {
		if(requestInvoker==null || requestInvoker.get()==null || responseInvoker == null || responseInvoker.get()==null) { 
			return;
		}
	}
	
	protected void removeCookie(String name) {
        logger.debug(getRemoteIp() + " removeCookie " + name);
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        if(domain != null && domain.length() > 0) {
            cookie.setDomain(domain);
        }
        responseInvoker.get().addCookie(cookie);
	}
	
	protected void setCookie(String name, String value) {
	    logger.debug(getRemoteIp() + " setCookie " + name + "=" + value);
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		if(domain != null && domain.length() > 0) {
			cookie.setDomain(domain);
		}
		responseInvoker.get().addCookie(cookie);
	}

	protected String getCookie(String name) {
		return getCookieMap().get(name);
	}
	
	protected TimeZone getClientTimezZone(TimeZone timeZone) {
		String tz = getCookie("timezone");
		return tz == null ? timeZone : TimeZone.getTimeZone(tz);
	}
	
	private String getHeader(String name) {
	    return requestInvoker.get().getHeader(name);
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
	        throw new BusinessException(ServiceError.NoGuest);
	    }
	    return userId;
	}

    protected String getRequesterUserId() {
        String userId = null;
        
        if(useHeader) {
            userId = getHeader("hu");
            return userId;
        }
        
        userId = getCookie("u");
        logger.debug("get cookie " + userId + " for ip " + getRemoteIp());
        if(userId == null) {
            return null;
        }
        
        if(encrypt) {
            DesEncrypter encrypter = new DesEncrypter(getRemoteIp());
            String decrypted = encrypter.decrypt(userId);
            
            if(decrypted == null) {
                logger.warn("cannot decrypt " + userId);
                removeUserCookie();
                return null;
            }
            userId = decrypted;
            return userId;
        }       
        else {
            return userId;
        }
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
	    if(useHeader) {
	        return;
	    }
	    
	    if(encrypt) {
    	    DesEncrypter encrypter = new DesEncrypter(getRemoteIp());
    	    String encrypted = encrypter.encrypt(userId);
    		setCookie("u", encrypted);
    		logger.debug("set user cookie " + encrypted);
	    }
	    else {
	        setCookie("u", userId);
            logger.debug("set user cookie " + userId);
	    }
	}
	
	protected void removeUserCookie() {
	    removeCookie("u");
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
}
