<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<div style="width:100%;">
<h3>Add a Store</h3>
</div>

<script type="text/javascript"
  src="http://maps.googleapis.com/maps/api/js?key=AIzaSyAOzJADcG1AcQUhAjs7gOLSzyO9uCiq7Ow&sensor=false">
</script>

<div style="position:absolute;right:32px;top:48px;">
	<div id="map_canvas" style="margin:8px;width:400px; height:300px;"></div>
</div>

<form onsubmit="return createStore();">
<table>
<o:text-input-tr label="name" id="name"/>
<o:text-input-tr label="address" id="address"><a href="javascript:void(0);" onclick="geo()">Pin It!</a></o:text-input-tr>
<o:text-input-tr label="address2" id="address2"/>
<o:text-input-tr label="city" id="city"/>
<o:text-input-tr label="state" id="state"/>
<o:text-input-tr label="country" id="country" value="US"/>
<o:text-input-tr label="zipcode" id="zipcode"/>
<o:text-input-tr label="phone" id="phone"/>
<o:text-input-tr label="email" id="email"/>
<o:text-input-tr label="web" id="web"/>
<o:text-input-tr label="categoryId" id="categoryId"/>
<o:text-input-tr label="logo" id="logo"/>
<o:text-input-tr label="latitude" id="latitude"/>
<o:text-input-tr label="longitude" id="longitude"/>
<o:submit-tr value="Create"/>
</table>
</form>

<script type="text/javascript">
function createStore() {
	var req = {
			name: $('#name').val(),
			address: $('#address').val(),
			address2: $('#address2').val(),
			city: $('#city').val(),
			state: $('#state').val(),
			country: $('#country" value="US').val(),
			zipcode: $('#zipcode').val(),
			phone: $('#phone').val(),
			email: $('#email').val(),
			web: $('#web').val(),
			categoryId: $('#categoryId').val(),
			logo: $('#logo').val(),
			latitude: $('#latitude').val(),
			longitude: $('#longitude').val()
	};
	var json = JSON.stringify(req);
	var url = serverBase + "/ws/store/add";
	$.ajax({
		  url: url,
		  type:"POST",
		  data:json,
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  complete: function(transport) {
			     if(transport.status == 200) {
			         self.location = serverBase + '/user/store/list/index.jsp.oo';
			     } else {
			         alert('Error: ' + transport.responseText);
			     }
			  }
		});

	return false;
}

$(document).ready(function(){
	initialize();
});

function initialize() {
	var myOptions = {
		center : new google.maps.LatLng(37.39064, -122.0318),
		zoom : 12,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map_canvas"),
			myOptions);
	google.maps.event.addListener(map, 'center_changed', function() {
		var C = map.getCenter();
		var X = C.lng();
		var Y = C.lat();
		document.getElementById('latitude').value = Y;
		document.getElementById('longitude').value = X;
	});
}

function geo() {
	var addr = $('#address').val() + "," + $('#city').val() + "," + $('#state').val();
	var geocoder = new google.maps.Geocoder();
	geocoder.geocode({
		'address' : addr
	}, function(results, status) {
		if (status == google.maps.GeocoderStatus.OK) {
			var latitude = results[0].geometry.location.lat();
			var longitude = results[0].geometry.location.lng();
			document.getElementById('latitude').value = latitude;
			document.getElementById('longitude').value = longitude;
			map.setCenter(new google.maps.LatLng(latitude, longitude));
		}
	});
}
</script>
