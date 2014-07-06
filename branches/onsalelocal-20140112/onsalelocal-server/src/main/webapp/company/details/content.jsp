<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>

<%
    String param = request.getQueryString();
    String url = "/ws/company?format=object&" + (param == null ? "" : param);;
    request.getRequestDispatcher(url).include(request, response);
%>

<h3>Company Profile</h3>

<table>
<o:tr-label-value label="Name"  value="${it.name }"/>
<o:tr-label-value label="Web"   value="${it.web }"/>
<o:tr-label-value label="Logo"  value="${it.logo }"><br/><img src='${it.logo }'/></o:tr-label-value>
<o:tr-label-value label="Phone" value="${it.phone}"/>
<o:tr-label-value label="Email" value="${it.email}"/>
</table>

<a href='<c:url value="/company/update/index.jsp.oo?companyId="/>${it.id}'>update</a>
<a href='<c:url value="/company/create/index.jsp.oo"/>'>create new</a>
