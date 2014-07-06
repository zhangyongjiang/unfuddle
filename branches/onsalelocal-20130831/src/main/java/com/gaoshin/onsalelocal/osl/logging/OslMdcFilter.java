package com.gaoshin.onsalelocal.osl.logging;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

public class OslMdcFilter implements Filter {
    private static final Logger logger = Logger.getLogger(OslMdcFilter.class);
    
    private static final String RegisterUri = "/user/register-";
    private static final int RegisterUriLen = RegisterUri.length();
    
    private static String hostname;
    static {
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        StatusExposingServletResponse resp = new StatusExposingServletResponse((HttpServletResponse)response);
        
        try {
            initMdc((HttpServletRequest)request, resp);
            chain.doFilter(request, resp);
            resp.flushBuffer();
        }
        finally {
            int status = resp.getStatus();
            MDC.put(MdcKeys.Status.name(), status);
            
            Object bytes = request.getAttribute("resp_bytes");
            if(bytes != null)
                MDC.put(MdcKeys.Bytes.name(), bytes);
            
            logger.info("done");
        }
    }

    private void initMdc(HttpServletRequest request, HttpServletResponse response) {
        resetMdc();
        MDC.put(MdcKeys.Method.name(), request.getMethod());
        parsedRequest(request, response);
    }
    
    public static void resetMdc() {
        for(MdcKeys mk : MdcKeys.class.getEnumConstants()) {
            MDC.remove(mk.name());
        }
        for(String key : MdcKeys.getAllKeys()) {
            MDC.remove(key);
        }
        
        MDC.put(MdcKeys.Hostname.name(), hostname);
        MDC.put(MdcKeys.StartTime.name(), String.valueOf(System.currentTimeMillis()));
        MDC.put(MdcKeys.TimeDiff.name(), new TimeDiff());
        MDC.put(MdcKeys.TimeSince.name(), new TimeSince());
        MDC.put(MdcKeys.ClientRequestTime.name(), System.currentTimeMillis());
        MDC.put(MdcKeys.Bytes.name(), 0);
    }

    public void parsedRequest(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
            	MdcKeys.addKey(c.getName());
                MDC.put(c.getName(), c.getValue());
            }
        }
        
        Enumeration headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String name = (String) headerNames.nextElement();
        	MdcKeys.addKey(name);
            MDC.put(name, request.getHeader(name));
        }
        
        String reqid = request.getHeader(MdcKeys.Reqid.name());
        if(reqid != null) {
            RequestId ar = new RequestId(reqid);
            MDC.put(MdcKeys.AppVer.name(), ar.getAppVersion());
            MDC.put(MdcKeys.DeviceId.name(), ar.getDeviceId());
            MDC.put(MdcKeys.ClientRequestTime.name(), ar.getTimestamp());
            MDC.put(MdcKeys.UserId.name(), ar.getUserId());
            MDC.put(MdcKeys.DeviceType.name(), ar.getDeviceType());
            MDC.put(MdcKeys.Latitude.name(), ar.getLatitude());
            MDC.put(MdcKeys.Longitude.name(), ar.getLongitude());
        }
        
        String uri = request.getRequestURI();
        MDC.put(MdcKeys.Uri.name(), uri);
        String query = request.getQueryString();
        if(query != null)
            MDC.put(MdcKeys.Query.name(), request.getQueryString());
        
        if(MDC.get(MdcKeys.Reqid.name()) == null) {
            reqid = UUID.randomUUID().toString();
            MDC.put(MdcKeys.Reqid.name(), reqid);
        }
        
        response.setHeader(MdcKeys.Reqid.name(), reqid);
    }
}
