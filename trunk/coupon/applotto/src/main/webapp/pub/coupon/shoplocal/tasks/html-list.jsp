<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/coupon" prefix="p" %>

<h3>Shoplocal Result List</h3>
<%
    String param = request.getQueryString();
    String url = "/ws/coupon/list-shoplocal-results?format=object&" + param;
    request.getRequestDispatcher(url).include(request, response);
%>
<c:if test="${empty it }">
    no html
</c:if>

<c:if test="${not empty it }">
    <table>
        <c:forEach var="result" items="${it }">
            <tr><td><a href='html.jsp.oo?id=${result.taskId }&page=${result.page}'>${result.page }</a></td></tr>
        </c:forEach>
    </table>
</c:if>

