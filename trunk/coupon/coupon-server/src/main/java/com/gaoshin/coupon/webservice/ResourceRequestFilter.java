package com.gaoshin.coupon.webservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gaoshin.coupon.db.UserContextHolder;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import common.util.web.BusinessException;
import common.util.web.ServiceError;

public class ResourceRequestFilter implements ContainerRequestFilter {
    @Context HttpServletRequest request;
    @Context HttpServletResponse response;

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        MultivaluedMap<String, String> map = request.getCookieNameValueMap();
        if(map == null || !map.containsKey("LOCATION")) {
            if(request.getRequestUri().toString().indexOf("set-location") == -1) {
                String loc = request.getHeaderValue("LOCATION");
                if(loc == null)
                    throw new BusinessException(ServiceError.LocationRequired);
                else
                    UserContextHolder.setUserState(loc);
            }
        }
        else {
            UserContextHolder.setUserState(map.get("LOCATION").toString());
        }
        return request;
    }

    public <T> T getBean(Class<T> type) {
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        return context.getBean(type);
    }
}
