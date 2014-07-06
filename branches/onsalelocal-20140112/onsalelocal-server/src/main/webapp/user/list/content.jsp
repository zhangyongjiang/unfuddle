<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>

<%
    String param = request.getQueryString();
    String url = "/ws/user/list?format=object&" + param;
    request.getRequestDispatcher(url).include(request, response);
%>

<osl:ws path="<%=url %>"/>
<h3>User List</h3>

<ul>
<c:forEach var="user" items="${it.items }">
    <li><a href='<c:url value="/user/profile/index.jsp.oo?userId="/>${user.id }'>${user.firstName } ${user.lastName }</li>
</c:forEach>
</ul>