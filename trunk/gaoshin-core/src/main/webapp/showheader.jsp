<%@page import="common.web.UserAgentTools"%>
<%@page import="java.util.Enumeration"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<%
Enumeration hnames = request.getHeaderNames();
String userAgent = null;
while(hnames.hasMoreElements()) {
    String name = (String)hnames.nextElement();
    String value = request.getHeader(name);
    out.write(name + ": " + value + "<br/>\n");
    if(name.equals("User-Agent"))
        userAgent = value;
}
if(userAgent!=null) {
    String[] os = UserAgentTools.getOS(userAgent);
    for(String s : os) {
    	out.write(s + ", ");
    }
    out.write("<br/>\n");
    
    String[] browser = UserAgentTools.getBrowser(userAgent);
    for(String s : browser) {
    	out.write(s + ", ");
    }
    out.write("<br/>\n");
}
%>