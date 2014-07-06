<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<%
	String param = request.getQueryString();
	String url = "/ws/user/files?format=object&" + (param == null ? "" : param);;
	request.getRequestDispatcher(url).include(request, response);
%>

<h3>Files</h3>

<c:if test="${empty it.items }">No file found</c:if>

<c:forEach var="file" items="${it.items }">
    <div style='margin-bottom:20px;'>
        <span id='file${file.id }'></span><br/>
        <img onload="document.getElementById('file${file.id }').innerHTML=this.src" style="width:150px;" src='<c:url value="/ws/v2/file/"/>${file.fileId}'/>
    </div>
</c:forEach>