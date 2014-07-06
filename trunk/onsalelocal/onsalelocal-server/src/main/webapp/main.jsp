<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<!DOCTYPE html>
<html lang="en">
	<head  profile="http://www.w3.org/2005/10/profile">
		<jsp:include page="meta.jsp.oo"></jsp:include> 
		<jsp:include page="style.css.oo"></jsp:include> 
		
		<script type="text/javascript">
		</script>
	</head>
	
	<body>
			<jsp:include page="navi.jsp.oo"></jsp:include>

    <div style="padding-left:15px;">            
			<jsp:include page="content.jsp.oo"></jsp:include>
			</div>
	</body>
</html>
