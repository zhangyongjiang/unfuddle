<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o"%>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g"%>

<style>
<!--
td {
	border-bottom: solid 1px;
}
-->
</style>
<h3>Client Logs</h3>

<jsp:include page="/notification/report/list?format=object&var=logs&start=${start }&size=${size }"></jsp:include>
<div style="clear: both;">
	<c:if test="${empty logs.list }">
	No log uploaded so far.
	</c:if>

	<c:if test="${not empty logs.list }">
		<table>
			<tr>
				<td>sentTime</td>
				<td>from</td>
				<td>type</td>
				<td>msg</td>
			</tr>
			<c:forEach var="post" items="${logs.list}">
				<tr>
					<td>${post.sentTime }</td>
					<td>${post.from.phone }</td>
					<td>${post.type }</td>
					<td><pre>${post.msg}</pre></td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
</div>

<a href='#' onclick='window.location="<o:url-replace key="start" value="0"></o:url-replace>"'>0</a>&nbsp;&nbsp;
<a href='#' onclick='window.location="<o:url-replace key="start" value="100"></o:url-replace>"'>100</a>&nbsp;&nbsp;
<a href='#' onclick='window.location="<o:url-replace key="start" value="200"></o:url-replace>"'>200</a>&nbsp;&nbsp;
<a href='#' onclick='window.location="<o:url-replace key="start" value="300"></o:url-replace>"'>300</a>&nbsp;&nbsp;
<a href='#' onclick='window.location="<o:url-replace key="start" value="400"></o:url-replace>"'>400</a>&nbsp;&nbsp;
<a href='#' onclick='window.location="<o:url-replace key="start" value="500"></o:url-replace>"'>500</a>&nbsp;&nbsp;
<a href='#' onclick='window.location="<o:url-replace key="start" value="600"></o:url-replace>"'>600</a>&nbsp;&nbsp;
