<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<div style="width:100%;margin-top:60px;text-align:center;min-height:300px;" >
	<button onclick="Device.startSimpleActivity('android.intent.action.VIEW','<o:url value="/m/download/gaoshin-android.apk"/>')" style="height:30%;width:60%;padding:20px;font-size:32px;">Download</button>
</div>
