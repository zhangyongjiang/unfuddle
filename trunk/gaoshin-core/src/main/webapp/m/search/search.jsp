<%@page import="com.gaoshin.beans.User"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<%
String location = "/dating/search?format=object&var=it";
%>

<div style="float:right;margin-top:-20px;">
<button data-inline="true" onclick='window.location="<c:url value="/m/search/criteria/index.jsp.oo"/>"'>Filters</button>
</div>

<h3>Match Result</h3>
<jsp:include page="<%=location%>"></jsp:include>

<div id="searchResult">

<c:if test="${empty it.list}">
No Match Found. 
</c:if>

<c:forEach var="user" items="${it.list }">
	<div onclick='window.location="<c:url value="/m/dating/profile/index.jsp.oo"/>?uid=${user.id}"' style='width:100%;clear:both;padding:6px 0px 6px 0px;border-bottom:solid 1px #aaa;'>
		<div style='float:left;margin-right:6px;'><a href='#' onclick='window.location="<c:url value="/m/dating/profile/index.jsp.oo"/>?uid=${user.id}"'><img width="60" src='<g:user-icon user="${user}"/>'/></a></div>
		<a href='#' onclick='window.location="<c:url value="/m/dating/profile/index.jsp.oo"/>?uid=${user.id}"'>${user.name}</a>
		<div>
			<div style='font-size:0.9em;color:#666;margin-top:6px;'>
			<span>${user.gender}, ${user.age} years old</span>
			</div>
			${user.interests}
			<c:if test="${not empty user.locationDistance }">
			<div style="margin-top:6px;color:#666;font-size:0.8em;font-weight:normal;">${user.locationDistance.city}, ${user.locationDistance.state } 
				<c:if test="${not empty user.locationDistance.distance}">
					<c:if test="${user.locationDistance.distance>100}">
					, > 100 miles
					</c:if>
					<c:if test="${user.locationDistance.distance<=100}">
					,<fmt:formatNumber type="number" maxFractionDigits="1" value="${user.locationDistance.distance}" /> miles
					</c:if>
				</c:if>
			</div>
			</c:if>
			<span style="color:#aaa;"> </span>
		</div>
		<div style="width:100%;clear:both;height:1px;">&nbsp;</div>
	</div>
</c:forEach>

</div>

