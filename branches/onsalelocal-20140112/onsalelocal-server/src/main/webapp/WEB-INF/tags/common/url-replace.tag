<%@tag import="java.util.HashSet"%>
<%@tag import="java.util.HashMap"%><%@tag import="java.net.URLEncoder"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="key" required="true" 
%><%@ attribute name="remove" required="false" 
%><%@ attribute name="url" required="false" 
%><%@ attribute name="value" required="true" 
%><%
    HashSet<String> removes = new HashSet<String>();
    if(remove != null) {
        for(String s : remove.split("[, ;]+")) {
            removes.add(s);
        }
    }
    
    StringBuilder sb = new StringBuilder();
    
    String current = request.getQueryString();
    if(url != null) {
    	if(url.startsWith("//"))
        	url = url.substring(1);
        else if (url.startsWith("/"))
        	url = request.getSession().getServletContext().getContextPath() + url;
        int pos = url.indexOf("?");
        if(pos != -1) {
            current = url.substring(pos+1);
            url = url.substring(0, pos);
        }
    }
    else {
        url = request.getRequestURI();
    }
    if(current != null) {
        for(String s : current.split("&")) {
            String[] kv = s.split("=");
            if(kv.length == 1) {
                continue;
            }
            if(removes.contains(kv[0])) {
                continue;
            }
            if(key.equals(kv[0])) {
                continue;
            }
            sb.append(s).append("&");
        }
    }
    sb.append(key).append("=").append(URLEncoder.encode(value, "UTF-8"));
    
    out.write(url + "?" + sb.toString());
%>
