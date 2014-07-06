<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o"%>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g"%>

<div data-role="header">
	<div data-role="navbar" data-theme="e" >
		<ul>
			<li><a href="#" data-icon="gear" data-iconpos="top" onclick="window.location='<c:url value="/m/dating/profile/index.jsp.oo"/>'" data-theme="e">Me</a></li>
			<li><a href="#" data-icon="search" onclick="window.location='<c:url value="/m/search/index.jsp.oo"/>'" data-iconpos="top" data-theme="e">Match</a></li>
			<li><a href="#" data-icon="info" data-iconpos="top" onclick="window.location='<c:url value="/m/misc/index.jsp.oo"/>'" data-theme="e">Info</a></li>
		</ul>
	</div>
</div>

<script type="text/javascript" >
function gomap() {
    var lat = $.query.get('lat');
    var lng = $.query.get('lng');
    var miles = $.query.get('miles');
    if(lat == null || lat.length == 0) {
        var lat = ${me.currentLocation.latitude};
        var lng = ${me.currentLocation.longitude};
        var miles = 15;
    }
    window.location = '<c:url value="/m/map/index.jsp.oo"/>?lat=' + lat + "&lng=" + lng + "&miles=" + miles;
}
</script>