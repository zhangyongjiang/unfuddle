<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<html>
	<head>
		<jsp:include page="meta.jsp.oo"></jsp:include> 
		<jsp:include page="style.css.oo"></jsp:include> 
		
		<script type="text/javascript">
		</script>
	</head>
	
	<body>
		<table><tr><td valign="top" style="border-right:solid 1px #fff;">
			<jsp:include page="navi.jsp.oo"></jsp:include>
			</td><td valign="top">
			<div style="padding-left:30px;">
				<jsp:include page="content.jsp.oo"></jsp:include>
			</div>
			</td></tr></table>
	</body>
</html>
