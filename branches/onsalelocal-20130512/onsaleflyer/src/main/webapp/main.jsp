<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<html>
	<head  profile="http://www.w3.org/2005/10/profile">
		<jsp:include page="meta.jsp.oo"></jsp:include> 
		<jsp:include page="style.css.oo"></jsp:include> 
		
		<script type="text/javascript">
		jQuery.fn.center = function () {
		    this.css("position","absolute");
		    this.css("top", Math.max(0, (($(window).height() - this.outerHeight()) / 2) + 
		                                                $(window).scrollTop() - 40) + "px");
		    this.css("left", Math.max(0, (($(window).width() - this.outerWidth()) / 2) + 
		                                                $(window).scrollLeft()) + "px");
		    return this;
		}
		</script>
	</head>
	
	<body onmousedown="return false;">
			<jsp:include page="navi.jsp.oo"></jsp:include>
            
			<div style="padding-left:30px;position:absolute;top:32px;left:0;right:0;">
				<jsp:include page="content.jsp.oo"></jsp:include>
			</div>
	</body>
</html>
