<%@page import="java.util.Enumeration"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<a href='javascript:void(0)' data-role="button" onclick='Device.exit()'>Exit</a>
<a href='#' data-role="button" onclick='window.location="<c:url value="/m/dating/profile/index.jsp.oo"/>"'>My Account</a>
<a href='#' data-role="button" onclick="Device.startSimpleActivity('android.intent.action.VIEW','<o:url value="/m/download/gaoshin-android.apk"/>')">Upgrade</a>

<c:if test="${me.name == 'Kevin' || me.name == 'Sam'}">
<div id='server8080' >
<a href="#" data-role="button" onclick="setServer('http://108.65.77.85:8080')">108.65.77.85:8080</a>
</div>

<div id='server9090' >
<a href="#" data-role="button" onclick="setServer('http://108.65.77.85:9090')">108.65.77.85:9090</a>
</div>

<a href="#" data-role="button" onclick="test()">Test</a>
<script type="text/javascript" >
function test() {
    window.location = '<c:url value="/m/test/index.jsp.oo"/>';
}
function setServer(server) {
    Device.setConf("SERVER_BASE_URL", server);
}
$(document).ready(function(){
    var server = Device.getConf("SERVER_BASE_URL");
    if(server == 'http://108.65.77.85:9090')
        $('#server9090').hide();
    if(server == 'http://108.65.77.85:8080')
        $('#server8080').hide();
});
</script>
</c:if>

<div data-role="collapsible" data-theme="c" data-collapsed="true">
	<h3>FAQ</h3>
	<ul data-role="listview" data-theme="c">
		<li><a href='#' onclick=''>ABC</a></li>
		<li><a href='#' onclick=''>DEF</a></li>
	</ul>
</div>

<div data-role="collapsible" data-theme="c" data-collapsed="true">
	<h3>Feedback</h3>
	<ul data-role="listview" data-theme="c">
		<li><a href='#' onclick=''>ABC</a></li>
		<li><a href='#' onclick=''>DEF</a></li>
	</ul>
</div>

<div data-role="collapsible" data-theme="c" data-collapsed="true">
	<h3>Terms of Service</h3>
	<jsp:include page="tos.jsp.oo"></jsp:include>
</div>


<c:if test="${me.name == 'Kevin' || me.name == 'Sam'}">
<div data-role="collapsible" data-theme="c" data-collapsed="true">
	<h3>HTTP Headers</h3>
	<p>
<%
	Enumeration e = request.getHeaderNames();
	while(e.hasMoreElements()) {
	    String name = e.nextElement().toString();
	    String value = request.getHeader(name);
	    out.write("<li>" + name + " = " + value + "</li>");
	}
%>
</p>
</div>

<div data-role="collapsible" data-theme="c" data-collapsed="true">
	<h3>Phone Info</h3>
	<p id="phoneinfo">
	<script type="text/javascript" >
		var pi = Device.getPhoneInfo();
		pi = JSON.parse(pi);
	    document.write("<li>cakkState " + pi.callState + "</li>");
	    document.write("<li>dataActivity " + pi.dataActivity + "</li>");
	    document.write("<li>dataState " + pi.dataState + "</li>");
	    document.write("<li>deviceId " + pi.deviceId + "</li>");
	    document.write("<li>deviceSoftwareVersion " + pi.deviceSoftwareVersion + "</li>");
	    document.write("<li>line1Number " + pi.line1Number + "</li>");
	    document.write("<li>networdCountryIso " + pi.networkCountryIso + "</li>");
	    document.write("<li>networkOperator " + pi.networkOperator + "</li>");
	    document.write("<li>networkOperatorName " + pi.networkOperatorName + "</li>");
	    document.write("<li>networkType " + pi.networkType + "</li>");
	    document.write("<li>phoneType " + pi.phoneType + "</li>");
	    document.write("<li>simCountryIso " + pi.simCountryIso + "</li>");
	    document.write("<li>simOperator " + pi.simOperator + "</li>");
	    document.write("<li>simOperatorName " + pi.simOperatorName + "</li>");
	    document.write("<li>simSerialNumber " + pi.simSerialNumber + "</li>");
	    document.write("<li>simState " + pi.simState + "</li>");
	    document.write("<li>subscriberId " + pi.subscriberId + "</li>");
	    document.write("<li>voiceMailAlphaTag " + pi.voiceMailAlphaTag + "</li>");
	    document.write("<li>voiceMailNumber " + pi.voiceMailNumber + "</li>");
	    document.write("<li>deviceWidth " + pi.deviceWidth + "</li>");
	    document.write("<li>deviceHeight " + pi.deviceHeight + "</li>");
	</script>
	</p>
</div>

<a href="http://192.168.192.168" data-role="button">t</a>

</c:if>