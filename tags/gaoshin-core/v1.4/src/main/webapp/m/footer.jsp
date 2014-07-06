<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=utf-8" 
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" 
%><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" 
%><%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" 
%><%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" 
%>
<div class="abc" id="footer" style="clear:both;z-index:99;position:absolute;width:100%;display:none;">
	<div data-type="horizontal" data-role="controlgroup" style="width:100%;margin:0;">
		<a href="#" onclick='window.location="<c:url value="/m/dating/profile/index.jsp.oo"/>"' data-theme="e" data-role="button" style="width:33%;">ME</a>
		<a href="#" onclick='window.location="<c:url value="/m/feedback/list/index.jsp.oo"/>"' data-theme="e" data-role="button" style="width:33%;">Feedback</a>
		<a href="#" onclick='window.location="<c:url value="/m/misc/index.jsp.oo"/>"' data-theme="e" data-role="button" style="width:33%;">Misc</a>
	</div>
	<div data-type="horizontal" data-role="controlgroup" style="width:100%;margin:0;">
		<a href="#" onclick='window.location="<c:url value="/m/poem/list/index.jsp.oo"/>"' data-theme="e" data-role="button" style="width:33%;">Poem</a>
		<a href="#" onclick='window.location="<c:url value="/m/map/index.jsp.oo"/>"' data-theme="e" data-role="button" style="width:33%;">Map</a>
		<a href="javascript:Device.exit()" data-theme="e" data-role="button" style="width:33%;">Exit</a>
	</div>	
	<c:if test="${me.name == 'Kevin' || me.name == 'Sam' || me.name == 'Cheng'}">
	<div data-type="horizontal" data-role="controlgroup" style="width:100%;margin:0;">
		<a data-role="button"  style="width:33%;" data-theme="e" href="#" onclick="window.location='<c:url value="/m/test/index.jsp.oo"/>'">TEST PAGE</a>
		<a data-role="button"  style="width:33%;" data-theme="e" href="#" onclick="alert(document.getElementsByTagName('html')[0].innerHTML)">SOURCE</a>
		<a data-role="button"  style="width:33%;" data-theme="e" href="#" onclick="window.location.reload()">Reload</a>
	</div>
	<script type="text/javascript" >
		try {Device.setDeviceLog(true);}catch(e){}
	</script>
	</c:if>
</div>

<script type="text/javascript">
$(document).ready(function(){
    if(Device.getClientType() == 'browser') {
        $('#footer').show();
    }
    
    <jsp:include page="/poem/random?format=object&var=randomPoem"></jsp:include>
    var pom = <o:obj2json obj="${randomPoem.content}"/>;
    var msg = {msg: pom.String};
    var json = JSON.stringify(msg);
    Device.addWidgetMessage(json);
});
</script>
