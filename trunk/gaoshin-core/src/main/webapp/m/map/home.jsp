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
		<div style="max-width:600px;width:100%;height:100%;" data-role="page" data-theme="e" id="jqm-home">
			<div id="content"  style="max-width:600px;width:100%;height:100%;">
			<iframe id="mapframe" name="mapframe" style="margin:0;padding:0;height:100%;width:100%;" src="_home.jsp?lat=<%=request.getParameter("lat")%>&lng=<%=request.getParameter("lng")%>"></iframe>
			</div>		
			<div style="clear:both;height:1px;">&nbsp;</div>
		</div>
	</body>
</html>
