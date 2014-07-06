<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>

<%
        String param = request.getQueryString();
        String url = "/ws/category/nearby?var=cats&format=object&" + (param == null ? "" : param);
        request.getRequestDispatcher(url).include(request, response);
%>

<c:if test="${not empty cats.items }">
<div style="float:left;margin-bottom:10px;width:96%;">
    <div><a href='<o:url-replace key="category" value=""></o:url-replace>'>All Categories</a></div>
    <c:forEach var="cat" items="${cats.items }">
        <div><a href='<o:url-replace remove="keywords" key="category" value="${cat.name }"></o:url-replace>'>${cat.name } (${cat.offerCount })</a></div>
    </c:forEach>
</div>
</c:if>