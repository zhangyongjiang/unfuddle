<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<title>|&lt;&mdash;&mdash;&mdash;&mdash; 25K Miles &mdash;&mdash;&mdash;&mdash;&gt;|</title>
	<meta name="description" content="Map application to find the distance between two points on a map, total length of a route, area of a polygon. Developed on Google Map API. "/>
	<meta name="keywords" content="maps,distance,length,area,points,calculate,find,crow,map,earth,km,mile,two,google,map,api,run, running, poi, flash, cycle, athlete, fitness, measure, pedometer, mashup, elevation, kilometer, meter,walking, distance, calculator, mapping, map, sports, training, biking, trithalon"/>

<link rel="stylesheet"  href='<c:url value="/jquery/mobile/jquery.mobile-1.0a3.css"/>' />
<script type="text/javascript" src='<c:url value="/script/json2.js"/>'></script>
<script type="text/javascript" src='<c:url value="/device.jsp"/>'></script>
<script type="text/javascript" src='<c:url value="/jquery/js/jquery-1.5.min.js"/>'></script>
<script type="text/javascript" src='<c:url value="/jquery/js/jquery.timer-1.2.js"/>'></script>
<script type="text/javascript" src='<c:url value="/jquery/js/jquery.query-2.1.7.js"/>'></script>

<style type="text/css">
#viewarea{
	height: 100%;
	width: 100%;
};

.labeledMarker {
};

.ui-icon-message {
	background-image: url(<c:url value="/m/images/message_24x24.png"/>);
}

.abc .ui-btn-inner {
	padding-left:6px;
	padding-right:6px;
}
</style>

<script type="text/javascript" >
var mapKeys = new Array();
mapKeys["localhost"] 	= "ABQIAAAAXJhUS5niXEXgEm0LZS219hTRzhAzSQ3u4E7LYXha2CwyHvWJ_RTeuAp71ID6EiffQcCKbkhd7bebAw";
mapKeys["xava.org"]			= "ABQIAAAAXJhUS5niXEXgEm0LZS219hRAYKnl5KH5KkIuBPVhxQdTCPJIIxR9JC0UBQ6NFWlda05eIZM0yGpEiQ";
mapKeys["inonemile.com"]	= "ABQIAAAAXJhUS5niXEXgEm0LZS219hS7xYvQ5EFlx7Dh09wFQIEshDGGUxRASWjYdeJBssB8Y-Km1MWl15kSvw";
mapKeys["25kmiles.com"]		= "ABQIAAAAXJhUS5niXEXgEm0LZS219hQMP3uD8ghGPdwLcT86JZJnarm9yxRXO1Y7cxOAGCXUm40WkXmXW3yTxg";
mapKeys["192.168.1.69"] = "ABQIAAAAXJhUS5niXEXgEm0LZS219hRYNrkc-vwrl2-qBPlm_c6TWfkZRxRH65f6sYWZDwKKmyjbfPUbgpL4Pw";
mapKeys["10.0.0.44"] = "ABQIAAAAXJhUS5niXEXgEm0LZS219hQ6wQ_RBoaY5Ra5_xxjY0lgC2NRBhSXhFSSnc5dg7k2NHeXceLH-0Ihgg";
mapKeys["108.65.77.85"] = 	"ABQIAAAAXJhUS5niXEXgEm0LZS219hQ0KmqIjvp0-_coMNWlCzl3p6ssQRT-n6HBMcTHqYE-XSeJa9I4vLsUvw";
for (var key in mapKeys) {
	if (window.location.href.indexOf(key) != -1) {
		var mapKey = mapKeys[key];
		break;
	}
}
document.write("<script type=\"text/javascript\" src=\"http://maps.google.com/maps?file=api&amp;v=2&amp;key=" + mapKey + "\" ><\/script>");
</script>	

<script type="text/javascript" src="PlaceManager.js"></script>
<script type="text/javascript" src="gogomap.js"></script>
<script type="text/javascript" >
var tiled = false;
function init() {
    var lat = '<%=request.getParameter("lat")%>';
    var lng = '<%=request.getParameter("lng")%>';
    if(lat.length>0) {
        lat = Number(lat);
        lng = Number(lng);
    } else {
        lat = null;
        lng = null;
    }
	gView.initView("viewarea", lat, lng);
	try {
		//if (gView.hasPassedInPlace) 
			gView.setMode('view');
		//else
		//	gView.setMode('edit');
	} catch (e) {}

	var gSchoolManager = new SchoolPlaceManager();
	gView.placeManager = gSchoolManager;
	gView.getDataTimer();
}

function getLocationCallback(address, place) {
    if(place == null) {
        alert("cannot find address " + address);
        return;
    }
    point = new GLatLng(place.Point.coordinates[1],
            place.Point.coordinates[0]);
	gView.map.setCenter(point);
}

function searchKeyDown (e) {
	var keynum
	var keychar
	var numcheck
	
	if(window.event) // IE
	{
		keynum = e.keyCode
	}
	else if(e.which) // Netscape/Firefox/Opera
	{
		keynum = e.which
	}
	keychar = String.fromCharCode(keynum);
	if (keychar == '\n' || keychar== '\r') {
		var addr = document.getElementById("searchAddr").value;
		gotoAddr(addr);
	}
}

function gotoListView() {
    var area = gView.getAreaString();
    window.location = '<c:url value="/user/search"/>?' + area;
}

function sendMessage() {
    var me = '${me.id}';
    try {
        var userId = (gView.getCurrentPlace().getCurrentMObj().getCurrentInfoMarker().uid);
        if(userId != me) {
        	top.location = '<c:url value="/m/message/send/index.jsp.oo?to="/>' + userId;
        }
    }catch (e) {
        alert("Please select an user to send message.");
    }
}

</script>

</head>
<body onload="init()" onunload="GUnload()">

<div style="z-index:0; position:absolute; top:0px; left:0px;" id="viewarea"></div>
<div id="messageIcon" style="z-index:1; position:absolute; top:6px; right:6px;padding:3px;border:solid 1px;background-color:white;font-size:36px;"><img width="40" height="40" onclick="sendMessage()" src="img/chat_40x40.jpg"/></div>
<div id="zoomInIcon" style="z-index:1; position:absolute; top:56px; right:6px;padding:3px;border:solid 1px;background-color:white;font-size:36px;"><img width="40" height="40" onclick="gView.zoomIn()" src="img/zoom_in_36x36.gif"/></div>
<div id="zoomOutIcon" style="z-index:1; position:absolute; top:106px; right:6px;padding:3px;border:solid 1px;background-color:white;font-size:36px;"><img width="40" height="40" onclick="gView.zoomOut()" src="img/zoom_out_36x36.gif"/></div>

<div id="centerLatLng" style="display:none;opacity:0.6;filter:alpha(opacity=60);z-index:1; position:absolute; right:6px; bottom:6px;padding:3px;border:solid 0px;background-color:white;">ddddddddddddddd</div>

</body>
</html>
