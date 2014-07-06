<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<div style="float:right;"><button onclick="openLocationDialog()">Add</button></div>

<c:if test="${empty it.list}">
You have no location defined currently. 
</c:if>


<c:if test="${not empty it.list}">
<table width="100%">
<c:forEach var="loc" items="${it.list }">

<c:if test="${loc.device }">
<tr><td><input type="radio" onchange="setCurrentLocation(${loc.id})" <c:if test="${loc.current }">checked</c:if> name="location" id="loc-${loc.id}" value="${loc.id}"></td>
<td><div style="margin-left:16px;"><a href="javascript:void(0)" onclick="Device.map('${loc.latitude},${loc.longitude}')">My Current Phone Location</a>
</div></td></tr>
</c:if>

<c:if test="${!loc.device }">
<tr><td><input type="radio" onchange="setCurrentLocation(${loc.id})" <c:if test="${loc.current }">checked</c:if> name="location" id="loc-${loc.id}" value="${loc.id}"></td>
<td><div style="margin-left:16px;"><a href="javascript:void(0)" onclick="Device.map('${loc.latitude},${loc.longitude}')">${loc.address }</a>
</div></td></tr>
</c:if>

</c:forEach>
</table>
</c:if>

<div id="newLocDialog" title="Add a new location">
<form style="padding: 0; margin: 0;" onsubmit="return addLocation();">
<textarea id="address" name="address" style="width: 100%; height: 80%;"></textarea>
</form>
</div>

<script type="text/javascript">
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

	$("#newLocDialog").dialog({
		autoOpen : false,
		//		height: 300,
		width : 300,
		modal : true,
		buttons : {
			"Add Location" : addLocation,
			Cancel : function() {
				$(this).dialog("close");
			}
		},
		close : function() {
			allFields.val("").removeClass("ui-state-error");
		}
	});

	// $("#add-location").button().click(openLocationDialog);
	
	function openLocationDialog() {
		$('#newLocDialog').dialog('option','position',[0,50]);
		$("#newLocDialog").dialog("open");
		$('#address').blur();
	}
	
	function setCurrentLocation(locationId) {
		$.ajax({
			url : serverBase + "/location/current/" + locationId,
			type : "POST",
			data : " ",
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
	}
/*
	$(document).ready(function(){
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

	});
*/
</script>