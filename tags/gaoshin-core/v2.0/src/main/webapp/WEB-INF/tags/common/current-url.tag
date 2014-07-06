<%@tag import="java.util.HashSet"
%><%@tag import="java.util.HashMap"
%><%@tag import="java.net.URLEncoder"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%
    StringBuilder sb = new StringBuilder();
    
    String current = request.getQueryString();
    if(current != null) {
        for(String s : current.split("&")) {
            String[] kv = s.split("=");
            if(kv.length == 1) {
                continue;
            }
            sb.append(s).append("&");
        }
    }
    out.write(request.getRequestURI() + "?" + sb.toString());
%>