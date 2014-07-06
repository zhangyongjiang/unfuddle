<%@tag import="common.util.DesEncrypter"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="data" required="true" 
%><%
	String ip = request.getRemoteAddr();
	String encrypted = new DesEncrypter(ip).encrypt64(data);
	out.write(encrypted);
%>