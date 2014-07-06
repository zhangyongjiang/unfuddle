<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<html>
	<head>
		<jsp:include page="meta.jsp.oo"></jsp:include> 
		<jsp:include page="style.css.oo"></jsp:include> 
		<!-- jsp:include page="google-analytics.jsp.oo"></ jsp:include -->
	</head>
	
	<body>
		<div id="viewarea" style="z-index:0; position:absolute; top:0px; left:0px;width:100%;height:100%;background-color:#fff0bb;"></div>
		<div data-role="page" data-theme="e" id="jqm-home">
			<!--jsp:include page="header.jsp.oo"></ jsp:include-->
			<div id="content" style="padding:0 0 0 3px;">
				<jsp:include page="content.jsp.oo"></jsp:include>
			</div>		
			<div style="clear:both;height:1px;">&nbsp;</div>
			<jsp:include page="footer.jsp.oo"></jsp:include>
		</div>
	</body>
</html>

</script>