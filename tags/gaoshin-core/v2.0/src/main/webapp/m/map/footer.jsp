<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<div style="z-index:99;left:1px;position: absolute; width: 100%;display:none;" id="footer" class="abc">
	<div class="ui-controlgroup ui-controlgroup-horizontal" data-type="horizontal" data-role="controlgroup" style="width: 100%; margin:0px;">
		<a class="ui-btn ui-btn-up-e" href="#" onclick='window.location="<c:url value="/m/dating/profile/index.jsp.oo"/>"' data-theme="e" data-role="button" style="width: 33%;"><span class="ui-btn-inner ui-corner-left"><span class="ui-btn-text">ME</span></span></a>
		<a class="ui-btn ui-btn-up-e" href="javascript:window.location='<c:url value="/m/feedback/list/index.jsp.oo"/>'" data-theme="e" data-role="button" style="width: 33%;"><span class="ui-btn-inner"><span class="ui-btn-text">Feedback</span></span></a>
		<a class="ui-btn ui-btn-up-e ui-controlgroup-last" href="javascript:window.location='<c:url value="/m/misc/index.jsp.oo"/>'" data-theme="e" data-role="button" style="width: 33%;"><span class="ui-btn-inner ui-corner-right ui-controlgroup-last"><span class="ui-btn-text">Misc</span></span></a>
	</div>
	<div class="ui-controlgroup ui-controlgroup-horizontal" data-type="horizontal" data-role="controlgroup" style="width: 100%;  margin:0px;">
		<a class="ui-btn ui-btn-up-e" href="javascript:window.location='<c:url value="/m/poem/list/index.jsp.oo"/>'" data-theme="e" data-role="button" style="width: 33%;"><span class="ui-btn-inner ui-corner-left"><span class="ui-btn-text">Poem</span></span></a>
		<a class="ui-btn ui-btn-up-e" href="javascript:window.location='<c:url value="/m/map/index.jsp.oo"/>'" data-theme="e" data-role="button" style="width: 33%;"><span class="ui-btn-inner"><span class="ui-btn-text">Map</span></span></a>
		<a class="ui-btn ui-controlgroup-last ui-btn-up-e" href="javascript:Device.exit()" data-theme="e" data-role="button" style="width: 33%;"><span class="ui-btn-inner ui-corner-right ui-controlgroup-last"><span class="ui-btn-text">Exit</span></span></a>
	</div>
	<c:if test="${me.name == 'Kevin' || me.name == 'Sam' || me.name == 'Cheng'}">
	<div class="ui-controlgroup ui-controlgroup-horizontal" data-type="horizontal" data-role="controlgroup" style="width: 100%;  margin:0px;">
		<a class="ui-btn ui-btn-up-e" href="javascript:window.location='<c:url value="/m/test/index.jsp.oo"/>'" data-theme="e" data-role="button" style="width: 33%;"><span class="ui-btn-inner ui-corner-left"><span class="ui-btn-text">TEST</span></span></a>
		<a class="ui-btn ui-btn-up-e" href="javascript:alert(document.getElementsByTagName('html')[0].innerHTML)" data-theme="e" data-role="button" style="width: 33%;"><span class="ui-btn-inner"><span class="ui-btn-text">Source</span></span></a>
		<a class="ui-btn ui-controlgroup-last ui-btn-up-e" href="#" onclick='window.location.reload()' data-theme="e" data-role="button" style="width: 33%;"><span class="ui-btn-inner ui-corner-right ui-controlgroup-last"><span class="ui-btn-text">Reload</span></span></a>
	</div>
	</c:if>
</div>

