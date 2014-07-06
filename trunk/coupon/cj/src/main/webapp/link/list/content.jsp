<%@page import="java.util.Properties"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/onsalelist" prefix="a" %>

<%
	String url = "/ws/cj/link-list?format=object&offset=0&size=100&" + request.getQueryString();
	request.getRequestDispatcher(url).include(request, response);
%>

<table>
<c:forEach var="link" items="${it.items }">
    <tr><td><a href='../details/index.jsp.oo?linkId=${link.id }'>${link.id }</a></td><td>${link.advertiserName }</td><td>${link.linkCodeHtml}</td></tr>
</c:forEach>
</table>

