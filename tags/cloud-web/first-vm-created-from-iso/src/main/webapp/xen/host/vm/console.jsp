<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
	String hostId = request.getParameter("hostId");
	String vmId = request.getParameter("vmId");
	String consoleId = request.getParameter("consoleId");
	String url = "/xen/host/vm/session/console/open?var=it&format=object&hostId=" + hostId + "&vmId=" + vmId + "&consoleId=" + consoleId;
	request.getRequestDispatcher(url).include(request, response);
%>

<html>
<head>
	<jsp:include page="jquery.jsp.oo"></jsp:include>
	<script type="text/javascript" >
		base = '<c:url value="/"/>';
		base = base.substring(0, base.length-1);
	</script>
</head>
<body style="padding: 0; margin:0;" onLoad="heartbeat()">
	<applet code="com/citrix/xenserver/console/Initialize.class" 
	    archive='<c:url value="/scripts/XenServerConsole-signed.jar"/>'
	  width="800" height="600">
		<PARAM NAME=SESSION VALUE="${it.sessionId }">
		<PARAM NAME=URL VALUE="${it.console.location }">
	    <PARAM NAME=USEURL VALUE="true">
	</applet>
	<img id="img_1_1" src='<c:url value="/images/1x1.gif"/>'/>
</body>
<script type="text/javascript">
function refresh() {
	var img = document.getElementById("img_1_1");
	var url = '<c:url value="/xen/host/vm/session/heartbeat"/>/${it.sessionId }';
	img.src = url + "?" + new Date().getTime();
	
    $.ajax({
        url : base + "/xen/host/vm/details?hostId=<%=hostId%>&vmId=<%=vmId%>",
        type : "GET",
        contentType : "application/json; charset=utf-8",
        dataType : "json",
        complete : function(transport) {
            if (transport.status == 200) {
                var obj = JSON.parse(transport.responseText);
                var uuid = "";
                try {
	                uuid = obj.consoleList[0].uuid;
                }
                catch (e) {
                }
                if(uuid != '<%=consoleId%>' && uuid.length > 0) {
                    window.location = "console.jsp?hostId=<%=hostId%>&vmId=<%=vmId%>&consoleId=" + uuid; 
                }
                else {
	            	heartbeat();
                }
            } else {
                alert('Error: ' + transport.status + ", "
                                + transport.responseText);
            }
        }
	});
}

function heartbeat() {
	setTimeout("refresh()", 3000);
}
</script>
</html>
