<%@page import="com.gaoshin.beans.User"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<div data-role="collapsible" data-theme="e" data-collapsed="false" style="padding-right:12px;">
<h3>Self Introduction</h3>
<c:if test="${me.id == user.id }">	
	<div style="float:right;margin-top:-52px;">
	<a href='#' onclick='window.location="<c:url value="/m/dating/profile/edit-intro/index.jsp.oo"/>"' data-role="button" data-icon="plus" data-theme="b" data-iconpos="notext">Edit</a> 
	</div>
</c:if>

<c:if test="${empty profile.introduction}">
	<p>N/A</p>
</c:if>
<c:if test="${not empty profile.introduction}">
	<p><o:html-escape value="${profile.introduction}" paragraph="true"/></p>
</c:if>
</div>
