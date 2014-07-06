<%@page import="com.gaoshin.beans.User"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<% 	
	User me = (User) request.getAttribute("me");
	String profileUrl = "/dating/profile/" + me.getId() + "?format=object&var=profile";
	String userId = request.getParameter("uid");
	if(userId == null)
	    userId = me.getId()+"";
	String pageUrl = "/user/profile-by-id?format=object&var=user&id=" + userId;
%>
<jsp:include page="<%=pageUrl%>"></jsp:include>
<jsp:include page="<%=profileUrl%>"></jsp:include>

<c:if test="${me.id == user.id }">
	<jsp:include page="allow-content.jsp.oo"></jsp:include>
</c:if>

<c:if test="${me.id != user.id }">
<c:if test="${profile.completeness <= 50 }">
	<jsp:include page="more-profile.jsp.oo"></jsp:include>
</c:if>

<c:if test="${profile.completeness > 50 }">
	<jsp:include page="allow-content.jsp.oo"></jsp:include>
</c:if>
</c:if>