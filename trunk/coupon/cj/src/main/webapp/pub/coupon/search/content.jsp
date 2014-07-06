<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/coupon" prefix="p" %>

<h3>Search Coupons</h3>

<script type="text/javascript"
  src="http://maps.googleapis.com/maps/api/js?key=AIzaSyAOzJADcG1AcQUhAjs7gOLSzyO9uCiq7Ow&sensor=false">
</script>

<form id="fsearch" onsubmit="return geo();">
<input type='hidden' name='latitude' id='latitude'/>
<input type='hidden' name='longitude' id='longitude'/>
<table style="width:100%;">
<tr><td>Close To:</td><td><input style="" type="text" id="addr" name="addr" value='<o:context name="addr"></o:context>' /></td></tr>
<tr><td>Radius:</td><td><input style="" type="text" id="radius" name="radius" value='<o:context name="radius" ></o:context>' /></td></tr>
<tr><td>Keywords:</td><td><input style="" type="text" id="keywords" name="keywords" value='<o:context name="keywords"></o:context>' /></td></tr>
<tr><td>Category:</td><td><jsp:include page="categories.jsp.oo"></jsp:include></td></tr>
</table>
<input type="submit" value="search"/>
</form>

<%
if(request.getParameter("latitude")!= null) {
	String param = request.getQueryString();
	String url = "/ws/coupon/by-geo?format=object&" + (param == null ? "" : param);
	request.getRequestDispatcher(url).include(request, response);
	Object data = request.getAttribute("it");
%>

<c:if test="${empty it.items}">
	No coupon found.
</c:if>

<c:if test="${not empty it.items}">
	${fn:length(it.items)} coupon(s) found.
	<c:forEach var="cp" items="${it.items}">
		<p:coupon-summary coupon="${cp }"/>
	</c:forEach>
</c:if>

<% } %>

<script type="text/javascript">
$(document).ready(function(){
});

function geo() {
	var lat = $('#latitude').val();
	var lng = $('#longitude').val();
	if(lat != null && lat.length > 0 && lng != null && lng.length > 0){
		return true;
	}
	var addr = $('#addr').val();
	if(addr == null || addr.length == 0) {
		alert("need a location.");
		return false;
	}
	else {
		var geocoder = new google.maps.Geocoder();
		geocoder.geocode({
			'address' : addr
		}, function(results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				var latitude = results[0].geometry.location.lat();
				var longitude = results[0].geometry.location.lng();
				document.getElementById('latitude').value = latitude;
				document.getElementById('longitude').value = longitude;
				$('#fsearch').submit();
			}
			else {
				alert("wrong address");
			}
		});
	}

	return false;
}
</script>
