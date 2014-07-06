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
			<jsp:include page="navi.jsp.oo"></jsp:include>
            
			<div style="padding-left:30px;position:absolute;top:32px;left:0;right:0;">
				<jsp:include page="content.jsp.oo"></jsp:include>
			</div>
	</body>
</html>
