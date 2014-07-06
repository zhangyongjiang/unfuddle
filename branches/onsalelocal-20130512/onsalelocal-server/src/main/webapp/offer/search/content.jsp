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
	String url = "/ws/offer/search?format=object&" + (param == null ? "" : param);
	request.getRequestDispatcher(url).include(request, response);
    String radius = request.getParameter("radius");
    if(radius==null || radius.trim().length()==0)
        radius = "10";
    String order = request.getParameter("order");
    if(order == null)
    	order = "DistanceAsc";
%>

<script type="text/javascript"
  src="http://maps.googleapis.com/maps/api/js?key=AIzaSyAOzJADcG1AcQUhAjs7gOLSzyO9uCiq7Ow&sensor=false">
</script>

<div style="float:left;margin:8px 8px -8px 0;">
<form onsubmit="return changeKeywords();">
	Keywords: <input type="text" name="keywords" id="keywords" size="30" value='<o:context name="keywords"/>'/>
</form>
</div>

<div style="float:right;margin:8px 36px -8px;">
<form onsubmit="return geo();">
	<input type="text" name="addr" id="addr" size="30"/>
	<button onclick="geo()">Change Location</button>
</form>
</div>
<div style="clear:both;height:1px;"></div>
<div id="map_canvas" style="margin:0;width:96%; height:300px;"></div>

<div style="margin-bottom:10px;width:96%;">
<div style="float:left;">Distance:</div>
<div style="float:left;margin-left:16px;"><input <% if("1".equals(radius)) out.write("checked");%> type="radio" onclick="changeRadius(1)"/> 1 Mile</div>
<div style="float:left;margin-left:16px;"><input type="radio" <% if("5".equals(radius)) out.write("checked");%> onclick="changeRadius(5)"/> 5 Miles</div>
<div style="float:left;margin-left:16px;"><input type="radio" <% if("10".equals(radius)) out.write("checked");%> onclick="changeRadius(10)"/> 10 Miles</div>
<div style="float:left;margin-left:16px;"><input type="radio" <% if("20".equals(radius)) out.write("checked");%> onclick="changeRadius(20)"/> 20 Miles</div>
<div style="float:left;margin-left:16px;"><input type="radio" <% if("50".equals(radius)) out.write("checked");%> onclick="changeRadius(50)"/> 50 Miles</div>
</div>

<div style="clear:both;height:1px;margin-bottom:20px;"></div>

<table style="width:100%;">
    <tr><td style="width:25%;" valign="top">
        <jsp:include page="stores.jsp"></jsp:include>
        <div style="clear:both;"></div>
        <jsp:include page="categories.jsp"></jsp:include>
    </td>
    <td style="width:75%;" valign="top">
		<c:if test="${empty it.items}">
		    No offer found.
		</c:if>
		
		<c:if test="${not empty it.items}">
		    ${fn:length(it.items)} deals(s) found.
		    <div style="float:right;margin-right:48px;">
		        <% if("DistanceAsc".equals(order)) {%>
		            <a href='javascript:void(0);'>[sort by distance]</a>&nbsp;&nbsp;&nbsp;
		            <a href='<o:url-replace key="order" value="UpdatedDesc"/>'>[sort by time]</a>
		        <% } else { %>
		            <a href='<o:url-replace key="order" value="DistanceAsc"/>'>[sort by distance]</a>&nbsp;&nbsp;&nbsp;
		            <a href='javascript:void(0);'>[sort by time]</a>
		        <% } %>
		    </div>
    
            <osl:offer-list items="${it.items }"/>
		</c:if>
        </td>
    </tr>
</table>
    
    <script type="text/javascript">
    </script>

<script type="text/javascript">
$(document).ready(function(){
	initialize();
	mapit();
});

function mapit() {
	<c:if test="${not empty it.items}">
	    <c:forEach var="deal" items="${it.items}">
	    var myLatLng = new google.maps.LatLng(${deal.latitude}, ${deal.longitude});
	    var beachMarker = new google.maps.Marker({
	        position: myLatLng,
	        map: map
	    });
	    </c:forEach>
    </c:if>
}

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
                        window.location = 'index.jsp.oo?lat=' + Y
                                + "&lng=" + X
                                + "&zoom=" + map.getZoom()
                                + '&radius=<o:context name="radius" defValue="10"/>'
                                + '&keywords=' + escape("<o:context name="keywords"/>")
                                ;
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
			window.location = 'index.jsp.oo?lat=' + latitude 
				+ "&lng=" + longitude 
				+ "&zoom=" + map.getZoom() 
				+ '&radius=<o:context name="radius" defValue="10"/>'
				+ '&keywords=' + escape("<o:context name="keywords"/>")
				;
		}
	});

	return false;
}

function bookmark(offerid) {
	var req = {
		offerId: offerid,
	};
	var json = JSON.stringify(req);
	var url = serverBase + "/ws/bookmark/add";
	$.ajax({
		  url:url,
		  type:"POST",
		  data:json,
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  complete: function(transport) {
			     if(transport.status == 200) {
			         window.location.reload();
			     } else {
			         alert('Error: ' + transport.status + ", " + transport.responseText);
			     }
			  }
		});

	return false;
}

function unbookmark(offerid) {
	var url = serverBase + "/ws/bookmark/remove/" + offerid;
	$.ajax({
		  url:url,
		  type:"POST",
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  complete: function(transport) {
			     if(transport.status == 200) {
			         window.location.reload();
			     } else {
			         alert('Error: ' + transport.status + ", " + transport.responseText);
			     }
			  }
		});

	return false;
}

function changeRadius(radius) {
                        window.location = 'index.jsp.oo?lat=<%=lat%>&lng=<%=lng%>'
                                + "&zoom=" + map.getZoom()
                                + '&radius=' + radius
                                + '&keywords=' + escape("<o:context name="keywords"/>")
                                + '&merchant=' + escape("<o:context name="merchant"/>")
                                + '&category=' + escape("<o:context name="category"/>")
    			;
}

function changeKeywords() {
                        window.location = 'index.jsp.oo?lat=<%=lat%>&lng=<%=lng%>'
                                + "&zoom=" + map.getZoom()
                                + '&radius=<o:context name="radius"/>'
                                + '&merchant=' + escape("<o:context name="merchant"/>")
                                + '&category=' + escape("<o:context name="category"/>")
				+ '&keywords=' + escape($('#keywords').val())
			;
	return false;
}
</script>

