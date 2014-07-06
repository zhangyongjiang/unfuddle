<%@page import="java.util.Map.Entry"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<%
try {
	java.util.Map<String, String[]> paraMap = request.getParameterMap();
	for(Entry entry : paraMap.entrySet()) {
	    String key = (String)entry.getKey();
	    String[] value = (String[])entry.getValue();
	    if(key == null || value==null || key.toString().length()==0 || value.length==0)
	        continue;
	    request.setAttribute(key, value[0]);
	}
} catch (Exception e) {}
%>

<jsp:include page="/user/current?format=object&var=me"></jsp:include>

<c:if test="${empty me.id}">
	<html>
	<head>
	<script type="text/javascript" >
		try {
		    Device.removeSessionCookies();
		} catch (e) {}
	</script>
	<meta http-equiv="refresh" content="0;url=<c:url value="/m/user/signup/index.jsp.oo"/>"> 
	</head>
	</html>
</c:if>
 
<c:if test="${not empty me.id}">
	<c:if test="${me.role == 'BADGUY'}">
		<jsp:include page="/m/badguy/home.jsp.oo"></jsp:include>
	</c:if>
	
	<c:if test="${me.role.permission <= 100}">
		<jsp:include page="_home.jsp.oo"></jsp:include>
	</c:if>
</c:if>
