<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>

<%
	String cat = request.getParameter("categoryId");
	String url = "/ws/v2/search?format=object&keywords=" + URLEncoder.encode(cat);
	request.getRequestDispatcher(url).include(request, response);
%>
<osl:ws path="<%=url %>"/>
<h3>Category: <%=cat %></h3>

<c:if test="${empty it.items }">No offer found</c:if>

<c:forEach var="offer" items="${it.items }">
    <div style='margin:20px;float:left;'>
        <osl:OfferDetails offer="${offer}"/>
    </div>
</c:forEach>

