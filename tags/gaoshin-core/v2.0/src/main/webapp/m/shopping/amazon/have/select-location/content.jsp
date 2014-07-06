<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o"%>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g"%>

<div style="text-align:center;">
<h2>I Offer</h2>
<div id="itemTitle"></div>
<h2>@</h2>
</div>

<form onsubmit="return ihave();"><jsp:include
	page="/location/my?view=select"></jsp:include>
	<div style="text-align:center;margin-top:20px;"><input type="submit" style="width:80%;height:3em;" value="submit"/></div>
</form>

<script type="text/javascript">
$(document).ready(function() {
	var asin = '<o:context name="asin"/>';
	var item = getAmazonItem(asin)
	item = getArray(item, ["Items", "Item"]);
	if(item != null) {
		$('#itemTitle').html(item[0].ItemAttributes.Title);
	}
});

	function ihave() {
		var location = $("input[@name='location']:checked");
		if(location == null || location.val() == null) {
			alert("please select a location.")
			return false;
		}
		location = location.val();
		var path = serverBase + '/shopping/amazon/have/<o:context name="asin"/>/' + location;
		$.ajax({
			url : path,
			type : "POST",
			data : " ",
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			complete : function(transport) {
				if (transport.status == 200) {
					window.location = '<c:url value="/shopping/amazon"/>/<o:context name="asin"/>';
				} else {
					alert('Error: ' + transport.status + ", "
							+ transport.responseText);
				}
			}
		});
		return false;
	}

	function addLocation() {
		var addr = $("#address").val();
		var latlng = '{"lat":"37.36883","lng":"-122.03634"}';
		try {
			latlng = Device.getGeocode(addr);
		} catch (e) {
			alert("Cannot locate your location. Address is wrong?");
			return;
		}
		latlng = JSON.parse(latlng);
		var lat = latlng.lat;
		var lng = latlng.lng;
		var req = {
			address : addr,
			latitude : lat,
			longitude : lng,
			device : false
		};
		var json = JSON.stringify(req);
		$.ajax({
			url : serverBase + "/location/new",
			type : "POST",
			data : json,
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			complete : function(transport) {
				if (transport.status == 200) {
					window.location.reload();
				} else {
					alert('Error: ' + transport.status + ", "
							+ transport.responseText);
				}
			}
		});

		return false;
	}
</script>