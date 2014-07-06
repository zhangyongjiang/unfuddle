package common.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.ResourceContext;

public class JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(JerseyBaseResource.class.getName());

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
	}
	
	@Context
	public void setResponseInvoker(ThreadLocal<HttpServletResponse> responseInvoker) {
		this.responseInvoker = responseInvoker;
	}
	
    protected void includeDotOO() throws IOException {
        StringBuilder sb = new StringBuilder();
        MultivaluedMap<String, String> queryParameters = hc.getRequest().getQueryParameters();
        for (Entry<String, List<String>> entry : queryParameters.entrySet()) {
            String key = entry.getKey();
            for (String value : entry.getValue()) {
                sb.append(key).append("=").append(value).append("&");
            }
        }

        String url = requestInvoker.get().getRequestURI() + "?" + sb.toString();
        responseInvoker.get().getWriter().write(url);
    }

	protected void setCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value == null ? "anything" : value);
		cookie.setPath("/");
        cookie.setMaxAge(value == null ? 0 : 1000000);
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
		HashMap<String, String> cookieMap = new HashMap<String, String>();
        if (requestInvoker != null && requestInvoker.get() != null) {
            Cookie[] cookies = requestInvoker.get().getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    cookieMap.put(c.getName(), c.getValue());
                }
            }
		}
		return cookieMap;
	}

    protected void setSessionAttribute(String name, Object value) {
        requestInvoker.get().getSession(true).setAttribute(name, value);
    }

    protected Object getSessionAttribute(String name) {
        if (requestInvoker == null)
            return null;
        if (requestInvoker.get() == null)
            return null;
        HttpSession session = requestInvoker.get().getSession();
        if (session == null)
            return null;
        else
            return session.getAttribute(name);
    }

	private void saveToAppContext(String key, Object value) {
		String addr = requestInvoker.get().getRemoteAddr();
        requestInvoker.get().getSession(true).getServletContext().setAttribute(addr + "." + key, value);
	}

	private Object getFromAppContext(String key) {
		try {
			String addr = requestInvoker.get().getRemoteAddr();
            return requestInvoker.get().getSession(true).getServletContext().getAttribute(addr + "." + key);
		} catch (Exception e) {
			return null;
		}
	}

    protected static List<String> split(String str) {
        return split(str, "[ ,;\t\n\r]+");
    }

    protected static List<String> split(String str, String pattern) {
        String[] items = str.split(pattern);
        List<String> ret = new ArrayList<String>();
        for (String s : items) {
            if (s.length() > 0)
                ret.add(s);
        }
        return ret;
    }

    protected static List<Long> splitLong(String str) {
        String[] items = str.split("[ ,;\t\n\r]+");
        List<Long> ret = new ArrayList<Long>();
        for (String s : items) {
            if (s.length() > 0)
                ret.add(Long.parseLong(s));
        }
        return ret;
    }

    protected static List<Integer> splitInt(String str) {
        String[] items = str.split("[ ,;\t\n\r]+");
        List<Integer> ret = new ArrayList<Integer>();
        for (String s : items) {
            if (s.length() > 0)
                ret.add(Integer.parseInt(s));
        }
        return ret;
    }

    protected <T> T getResource(Class<T> resourceClass) {
        return resourceContext.getResource(resourceClass);
    }
}
