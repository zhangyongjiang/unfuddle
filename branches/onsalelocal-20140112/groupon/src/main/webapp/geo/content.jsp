<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>
<%
	String param = request.getQueryString();
    String lat = request.getParameter("lat");
    if(lat == null)
        lat = "37.39064";
    String lng = request.getParameter("lng");
    if(lng == null)
        lng = "-122.0318";
    String zoom = request.getParameter("zoom");
    if(zoom == null)
        zoom = "12";
	String url = "/ws/groupon/deals-by-geo?format=object&" + (param == null ? "" : param);
	request.getRequestDispatcher(url).include(request, response);
%>

<script type="text/javascript"
  src="http://maps.googleapis.com/maps/api/js?key=AIzaSyAOzJADcG1AcQUhAjs7gOLSzyO9uCiq7Ow&sensor=false">
</script>

<form onsubmit="return geo();">
<div style="margin:8px;">
	<input type="text" name="addr" id="addr" size="75"/>
	<button onclick="geo()">Change Location</button>
</div>
<div id="map_canvas" style="margin:8px;width:96%; height:300px;"></div>
</form>

<c:if test="${empty it.deals.deal}">
	No offer found.
</c:if>

<c:if test="${not empty it.deals.deal}">
        ${fn:length(it.deals.deal)} deals(s) found.
    <table style="border:solid 1px;width:96%;">
        <c:forEach var="deal" items="${it.deals.deal}">
        <tr>
            <td style="padding:3px;border-top:solid 1px #ccc;"><img src='${deal.mediumImageUrl}'/></td>
            <td style="width:50%;padding:3px;border-top:solid 1px #ccc;valign:top;"><a href='../deal-details/index.jsp.oo?dealId=${deal.id }'>${deal.title}</a></td>
            <td style="padding:3px;border-top:solid 1px #ccc;">${deal.merchant.name}
				<div style="font-size:0.8em;color:#666;">${deal.options.option[0].redemptionLocations.redemptionLocation[0].streetAddress1}</div>
				<div style="font-size:0.8em;color:#666;">${deal.options.option[0].redemptionLocations.redemptionLocation[0].city},
				${deal.options.option[0].redemptionLocations.redemptionLocation[0].state}</div>
				<div style="font-size:0.8em;color:#666;">${deal.options.option[0].redemptionLocations.redemptionLocation[0].phoneNumber}</div>
            </td></tr>
        </c:forEach>
    </table>
</c:if>

<script type="text/javascript">
$(document).ready(function(){
	initialize();
});

eventTime = 0;
X = 0;
Y = 0;
function initialize() {
	var myOptions = {
		center : new google.maps.LatLng(<%=lat%>, <%=lng%>),
		zoom : <%=zoom%>,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	google.maps.event.addListener(map, 'center_changed', function() {
		var C = map.getCenter();
		X = C.lng();
		Y = C.lat();
        eventTime = new Date().getTime();
        setTimeout("changeLocation(" + eventTime + ")", 300);
	});
}

function changeLocation(time) {
    if(time != eventTime) {
        return;
    }
    window.location = 'index.jsp.oo?lat=' + Y + "&lng=" + X + "&zoom=" + map.getZoom() + "&radius=10";
}

function geo() {
	var addr = $('#addr').val();
	var geocoder = new google.maps.Geocoder();
	geocoder.geocode({
		'address' : addr
	}, function(results, status) {
		if (status == google.maps.GeocoderStatus.OK) {
			var latitude = results[0].geometry.location.lat();
			var longitude = results[0].geometry.location.lng();
    window.location = 'index.jsp.oo?lat=' + latitude + "&lng=" + longitude + "&zoom=" + map.getZoom() + "&radius=10";
		}
	});

	return false;
}
</script>
