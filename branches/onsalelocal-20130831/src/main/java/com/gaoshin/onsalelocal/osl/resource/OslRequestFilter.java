package com.gaoshin.onsalelocal.osl.resource;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import common.db.dao.ConfigDao;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

public class OslRequestFilter  implements ContainerRequestFilter {
    @Context HttpServletRequest request;
    @Context HttpServletResponse response;

    private int verMin = -1;
    private int verMax = -1;
    
    @Override
    public ContainerRequest filter(ContainerRequest creq) {
    	System.out.println(creq.getRequestUri().toString());
    	String clientVer = null;
    	String clientVerCookie = null;
    	if(clientVer == null)
    		clientVer = request.getParameter("ver");
    	if(clientVer == null)
    		clientVerCookie = clientVer = creq.getCookieNameValueMap().getFirst("ver");
    	if(clientVer == null)
    		clientVer = creq.getHeaderValue("ver");
    	if(clientVer == null)
    		throw new BusinessException(ServiceError.InvalidInput, "need client version number");
    	
    	if(clientVer != null && !clientVer.equals(clientVerCookie)) {
    		Cookie cookie = new Cookie("ver", clientVer);
    		cookie.setMaxAge(1000000);
			response.addCookie(cookie );
    	}
    	
    	if(verMin == -1) {
	    	ConfigDao configDao = getBean(ConfigDao.class);
			String min = configDao.get("version.min", "0");
			String max = configDao.get("version.max", "999999");
			verMin = Integer.parseInt(min);
			verMax = Integer.parseInt(max);
    	}
    	
    	int ver = Integer.parseInt(clientVer);
    	if(ver < verMin || ver > verMax) {
    		throw new BusinessException(ServiceError.Deprecated, "no longer supported. please upgrade");
    	}
    	
        return creq;
    }

    public <T> T getBean(Class<T> type) {
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        return context.getBean(type);
    }
}
