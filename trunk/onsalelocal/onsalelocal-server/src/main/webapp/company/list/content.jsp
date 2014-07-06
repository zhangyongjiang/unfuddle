<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>

<%
    String param = request.getQueryString();
    String url = "/ws/company/list?format=object&" + (param == null ? "" : param);;
    request.getRequestDispatcher(url).include(request, response);
%>

<h3>Company List</h3>

<table>
<c:forEach var="item" items="${it.items }">
<div style="margin-bottom:8px;border:solid 1px;padding:8px;margin:8px;">
<a href='../details/index.jsp.oo?companyId=${item.id }'>
${item.name }<br/></a>
${item.phone }<br/>
<a href='${item.web}'>${item.web }</a><br/>
${item.logo }<br/>
<img  src='${item.logo }'/>
</div>
<% out.flush(); %>
</c:forEach>
</table>

