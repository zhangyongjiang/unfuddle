<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/coupon" prefix="p" %>

<h3>Shoplocal Tasks</h3>

<form id="fsearch" >
<table style="width:100%;border:solid 1px;">
<tr><td style="border:solid 1px #bbb;padding:3px;">Status:</td><td style="border:solid 1px #bbb;padding:3px;">
    <select name="status">
        <option value="">ANY</option>
        <option value="Pending">Pending</option>
        <option value="HtmlReady">HtmlReady</option>
        <option value="Processing">Processing</option>
        <option value="Failed">Failed</option>
        <option value="Fetched">Fetched</option>
    </select>
</td></tr>
<tr><td style="border:solid 1px #bbb;padding:3px;">Size:</td><td style="border:solid 1px #bbb;padding:3px;">
    <input type="text" name="size" value="100"/>
</td></tr>
</table>
<input type="submit" value="search"/>
</form>

<%
	String param = request.getQueryString();
	String url = "/ws/coupon/list-shoplocal-tasks?format=object&" + (param == null ? "" : param);
	request.getRequestDispatcher(url).include(request, response);
	Object data = request.getAttribute("it");
%>

<c:if test="${empty it}">
	No shoplocal task found.
</c:if>

<c:if test="${not empty it}">
	${fn:length(it)} found.
<table style="width:100%;border:solid 1px;">
        <tr>
            <td style="border:solid 1px #bbb;padding:3px;">ID</td>
            <td style="border:solid 1px #bbb;padding:3px;">City</td>
            <td style="border:solid 1px #bbb;padding:3px;">URL</td>
            <td style="border:solid 1px #bbb;padding:3px;">Page</td>
            <td style="border:solid 1px #bbb;padding:3px;">Status</td>
            <td style="border:solid 1px #bbb;padding:3px;">HTML</td>
        </tr>
	<c:forEach var="cp" items="${it}">
		<tr>
            <td style="border:solid 1px #bbb;padding:3px;">${cp.id }</td>
            <td style="border:solid 1px #bbb;padding:3px;">${cp.city }</td>
            <td style="border:solid 1px #bbb;padding:3px;">${cp.url }</td>
            <td style="border:solid 1px #bbb;padding:3px;">${cp.page }</td>
            <td style="border:solid 1px #bbb;padding:3px;">${cp.status }</td>
            <td style="border:solid 1px #bbb;padding:3px;"><c:if test="${not empty cp.html}"><a target="html" href='html-list.jsp.oo?id=${cp.id }'>HTML</a></c:if></td>
        </tr>
	</c:forEach>
    </table>
</c:if>

<script type="text/javascript">
$(document).ready(function(){
});

</script>
