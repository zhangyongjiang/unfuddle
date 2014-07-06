<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o"%>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl"%>

<%
        String url = "/ws/offer/home?format=object&var=latest&";
        request.getRequestDispatcher(url).include(request, response);

%>

<script type="text/javascript"
  src="http://maps.googleapis.com/maps/api/js?key=AIzaSyAOzJADcG1AcQUhAjs7gOLSzyO9uCiq7Ow&sensor=false">
</script>

<style type="text/css">
	.city {
		float:left;
		min-width:160px;
		padding:8px;
		border-bottom:solid 1px #eee;
	}
</style>

<table style="width:100%;">
<tr><td style="width:50%;" valign="top">
<h3>Latest Deals</h3>
<osl:offer-list items="${latest.items}"/>
</td>

<td valign="top">
<h3>Major Cities:</h3>
<div style="clear:both;padding-left:8px;"><form onsubmit="return changeLoc();"><input size="48" id="loc" placeholder="please type city,state or zipcode" type="text"><input type="submit" value="go"/></form></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Akron/Canton</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Albany</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Albuquerque</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Allentown/Reading</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Atlanta</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Augusta</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Austin</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Baltimore</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Baton Rouge</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Birmingham</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Boise</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Boston</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Buffalo</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Calgary</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Cedar Rapids / Iowa City</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Central Jersey</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Charleston</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Charlotte</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Chicago</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Cincinnati</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Cleveland</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Colorado Springs</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Columbus</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Dallas</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Dayton</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Denver</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Detroit</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Edmonton</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Fort Collins</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Fort Lauderdale</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Fort Myers / Cape Coral</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Fresno</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Grand Rapids</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Greenville</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Hampton Roads</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Harrisburg / Lancaster</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Hartford</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Honolulu</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Houston</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Huntsville</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Indianapolis</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Jacksonville</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Kansas City</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Knoxville</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Lexington</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Los Angeles</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Louisville</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Lubbock</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Madison</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Memphis</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Miami</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Milwaukee</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Minneapolis</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Mississauga</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Montreal</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Nashville</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">New Orleans</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">North Jersey</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Oakland</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Ocala</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Oklahoma City</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Omaha</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Orange County</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Orlando</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Ottawa</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Palm Springs</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Philadelphia</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Phoenix</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Piedmont Triad</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Pittsburgh</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Portland</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Providence</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Reno</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Richmond</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Rochester</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Sacramento</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Salt Lake City</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">San Antonio</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">San Fernando Valley</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">San Francisco</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">San Jose</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Santa Barbara</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Seattle</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Shreveport/Bossier</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">South Jersey</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Spokane</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Stockton</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Syracuse</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Tallahassee</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Tampa St Petersburg</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Toledo</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Toronto</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Tucson</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Tulsa</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Vancouver</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Victoria</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Washington DC</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Winnipeg</a></div>
<div class="city"><a href="javascript:void(0);" onclick="geo(this)">Worcester</a></div>
</td>
</tr>
</table>
<div style="clear:both;height:1px;"></div>

<script type="text/javascript">
function geo(elem) {
	var addr = elem.firstChild.nodeValue;
	go(addr);
}

function go(addr) {
	var geocoder = new google.maps.Geocoder();
	geocoder.geocode({
		'address' : addr
	}, function(results, status) {
		if (status == google.maps.GeocoderStatus.OK) {
			var latitude = results[0].geometry.location.lat();
			var longitude = results[0].geometry.location.lng();
			window.location = serverBase + '/offer/search/index.jsp.oo?lat=' + latitude 
				+ "&lng=" + longitude 
				+ "&zoom=" + 12 
				+ '&radius=<o:context name="radius" defValue="10"/>'
				;
		}
	});

	return false;
}

function changeLoc() {
	go($('#loc').val());
	return false;
}
</script>
