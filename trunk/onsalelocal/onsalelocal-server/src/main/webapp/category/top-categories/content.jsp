<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>

<%
	String url = "/ws/category/top-categories?format=object&";
	request.getRequestDispatcher(url).include(request, response);
%>
<osl:ws path="<%=url %>"/>
<h3>Categories</h3>

<c:forEach var="cat" items="${it.items }">
    <div style="float:left; min-width:200px;margin:5px;padding:5px;"><a target="_self"
        href='<c:url value="/category/list/index.jsp.oo?categoryId="/><o:url-encode value="${cat.id}"/>'>${cat.id}</a>
    </div>
</c:forEach>
