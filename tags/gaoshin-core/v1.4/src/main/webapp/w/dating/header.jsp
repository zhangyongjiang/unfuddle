<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<div style="text-align:center;">
<button id="btn-account" style="">Account</button>
<button id="btn-search" style="">Search</button>
<button id="btn-home" style="">Exit</button>
</div>

<script type="text/javascript">
$(document).ready(function() {
	var dw = Device.getDeviceWidth();
	var itemWidth = dw / 3 - 20;
	$("#btn-account").button().click(function() {
		window.location = "/user/profile";
	});
	
	$("#btn-home").button().click(function() {
		Device.exit();
	});
	
	$("#btn-search").button().click(function() {
		window.location = "/m/shopping/search/index.jsp.oo";
	});

});

</script>