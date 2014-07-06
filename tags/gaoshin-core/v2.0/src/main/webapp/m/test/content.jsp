<%@page import="java.util.Enumeration"%><%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<a href='#' onclick='window.location="<c:url value="/m/poem/list/index.jsp.oo"/>"' data-role="button">Poem</a>
<a href='#' onclick='window.location="<c:url value="/m/sms/send/index.jsp.oo"/>"' data-role="button">SEND SMS</a>
<a href='#' onclick='window.location="<c:url value="/m/sms/receive/index.jsp.oo"/>"' data-role="button">RECEIVE SMS</a>
<a href='#' onclick='window.location="<c:url value="/m/content-provider/index.jsp.oo"/>"' data-role="button">Content Provider</a>
<a href='#' onclick='window.location="<c:url value="/m/user/search/index.jsp.oo"/>"' data-role="button">Search</a>
<a href='#' onclick='window.location="<c:url value="/m/user/most-recent/index.jsp.oo"/>"' data-role="button">Most Recents</a>
<a href='#' onclick='Device.toast("hello")' data-role="button">Toast</a>
<a href='#' onclick='Device.removeAllCookies()' data-role="button">Delete Cookies</a>
<a href='#' onclick="alert(document.getElementsByTagName('html')[0].innerHTML)" data-role="button">HTML</a>
<a href='#' data-role="button" onclick="Device.startSimpleActivity('android.intent.action.VIEW','<o:url value="/m/download/gaoshin-android.apk"/>')">Upgrade</a>

<div id='server8080' >
<a href="#" data-role="button" onclick="setServer('http://108.65.77.85:8080')">108.65.77.85:8080</a>
</div>

<div id='server9090' >
<a href="#" data-role="button" onclick="setServer('http://108.65.77.85:9090')">108.65.77.85:9090</a>
</div>

<a href="javascript:void(0)" data-role="button" onclick="test()">Test</a>

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


<script type="text/javascript" >
function test() {
    //Device.setConf("HOME_URL", "http://jfkdjfadjfdfd.com");
    //setLoadingMessage();
    //sms('4085987655', "abc\ndef\nhij");
    //dial('4085987655');
}
function setLoadingMessage() {
    var str = "You said we'd always be together\n"
        + "And that gave me so much pleasure\n"
        + "\n"
        + "You said you'd never tell me a lie\n"
        + "And that made me want to cry";
    Device.addWidgetMessage(str);
}
function setServer(server) {
    Device.setConf("HOME_URL", server);
}
$(document).ready(function(){
    var server = Device.getConf("HOME_URL");
    if(server == 'http://108.65.77.85:9090')
        $('#server9090').hide();
    if(server == 'http://108.65.77.85:8080')
        $('#server8080').hide();
});
</script>
