<%@page import="java.util.Random"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<jsp:include page="/poem/random?format=object&var=poem"></jsp:include>

<% Random random = new Random(System.currentTimeMillis()); 
	int index = random.nextInt(100) + 1;
	String contextPath = request.getContextPath();
	String fileName = "/m/poem/images/" + index + ".jpg";
%>
<div style="max-width:640px;width:100%;overflow:hidden;position:absolute;top:50px;left:0px;z-index:0;padding:0;"><img style="width:100%;opacity:0.45;filter:alpha(opacity=45)" border="0" src='<c:url value="<%=fileName%>"/>'/></div>

<% pageContext.setAttribute("newLineChar", "\n"); %>
<div style="padding:16px;font-family:'courier new';font-weight:bold;">
	<div style="clear:both; margin-bottom:20px;font-size:0.8em;">
		<c:if test="${not empty poem.title }">
			- by ${poem.title }
		</c:if>
		<c:if test="${empty poem.title }">
			- by anonymous 
		</c:if>
	</div>
	
	${fn:replace(poem.content, newLineChar, "<br/><br/>")}
	
</div>

