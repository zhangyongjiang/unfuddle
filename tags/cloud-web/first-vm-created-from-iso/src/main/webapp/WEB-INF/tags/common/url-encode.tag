<%@tag import="java.net.URLEncoder"%><%@ attribute name="value" required="true" 
%><%
	out.write(URLEncoder.encode(value, "UTF-8"));
%>