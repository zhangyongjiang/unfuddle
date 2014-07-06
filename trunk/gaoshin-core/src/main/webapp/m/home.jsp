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
		
		<script type="text/javascript">
		$(document).ready(function(){
		    if(Device.getClientType() == 'browser') {
		        $('#jqm-home').css({"border":"solid 1px #ccc"});
		    }
		});
		</script>
	</head>
	
	<body>
		<div id="viewarea" style="max-width:600px;z-index:0; position:absolute; top:0px; left:0px;width:100%;height:100%;background-color:#fff0bb;"></div>
		<div style="max-width:600px;" data-role="page" data-theme="e" id="jqm-home">
			<jsp:include page="header.jsp.oo"></jsp:include>
			<div id="content" style="padding:0 0 0 3px;">
				<jsp:include page="content.jsp.oo"></jsp:include>
			</div>		
		</div>
	</body>
</html>
