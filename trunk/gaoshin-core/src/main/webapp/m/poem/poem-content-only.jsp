<%@page import="java.util.Random"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<div style="padding:16px;font-family:'courier new';font-weight:bold;">
	${fn:replace(poem.content, newLineChar, "<br/><br/>")}
	
	<c:if test="${not empty poem.title }">
		<div style="clear:both; margin-top:20px;font-size:0.8em;">
		- by ${poem.title }
		</div>
	</c:if>
</div>

