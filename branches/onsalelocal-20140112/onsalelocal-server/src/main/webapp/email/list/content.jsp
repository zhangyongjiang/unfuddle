<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%
    String param = request.getQueryString();
    String url = "/ws/email/list?format=object&" + (param == null ? "" : param);
    request.getRequestDispatcher(url).include(request, response);
    Object data = request.getAttribute("it");
%>

<h3>Email Offers</h3>

<c:forEach var="eo" items="${it }">
    <div style="margin-bottom:16px;">
        <o:millisecond-to-date time="${eo.updated}"/>&nbsp;&nbsp;&nbsp;&nbsp;
        <a href='../details/index.jsp.oo?id=${eo.id}'>${eo.subject }</a>&nbsp;&nbsp;&nbsp;&nbsp;
        <a href='../original/index.jsp.oo?id=${eo.id}'>[ original ]</a>
    </div>
</c:forEach>
