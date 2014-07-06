<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<html>
	<head>
		<jsp:include page="meta.jsp.oo"></jsp:include> 
	</head>
	
	<body>
		<jsp:include page="header.jsp.oo"></jsp:include>
		<div style='clear:both;height:1px;margin-bottom:6px;'>&nbsp;</div>
		<jsp:include page="navi.jsp.oo"></jsp:include>
		<jsp:include page="content.jsp.oo"></jsp:include>
		
		<div class="seperator">&nbsp;</div>
		<jsp:include page="footer.jsp.oo"></jsp:include>
	</body>
</html>