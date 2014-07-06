<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
	String hostId = request.getParameter("hostId");
	String templateId = request.getParameter("templateId");
	String url = "/xen/host/vm/details?hostId=" + hostId + "&vmId=" + templateId + "&format=object&var=template";
	request.getRequestDispatcher(url).include(request, response);
	url = "/xen/host/details/" + hostId + "?format=object&var=host";
	request.getRequestDispatcher(url).include(request, response);
%>
<jsp:include page="<%=url %>"></jsp:include>

<div class="page-header">Create VM</div>

<c:if test="${!template.template}">
Not a template
</c:if>

<c:if test="${template.template}">

<form onsubmit="return createVm()">
<table>

<tr>
<td>Name: </td><td><input type="text" name="name" id="name"/></td>
</tr>

<tr>
<td>Template: </td><td>${template.nameLabel }</td>
</tr>

<tr>
<td>Install from: </td><td>
	<jsp:include page="vdi-list.jsp.oo"></jsp:include>
</td>
</tr>

<tr>
<td>CPU #: </td><td>
	<input type="text" name="cpus" id="cpus" value="1" />
</td>
</tr>

<tr>
<td>Memory: </td><td>
	<input type="text" name="memory" id="memory" value="1024000000" />
</td>
</tr>

<tr>
<td>Virtual Disk: </td><td>
	<jsp:include page="sr-list.jsp.oo"></jsp:include>
	<input type="text" name="vdSize" id="vdSize" value="8000000000" />
</td>
</tr>

<tr>
<td>Network: </td><td>
	<jsp:include page="network-list.jsp.oo"></jsp:include>
</td>
</tr>

</table>

<input type="submit" value="Create"/>
<input type="button" value="Cancel" onClick="window.history.back()" />
</form>
</c:if>


<script type="text/javascript">
function createVm() {
	var hostId = "<%=hostId%>";
	var vmId = "<%=templateId%>";
	var name = $('#name').val();
	if(name == null || name.length == 0) {
		alert("Invalid name");
		return;
	}
	$("#btnClone").attr("disabled", "disabled");
	var req = {
	    hostId: hostId,
	    templateId: vmId,
	    vmName: name,
	    vdiUuid: $('#vdiList').val()
	}
	var json = JSON.stringify(req);
    $.ajax({
        url : base + "/xen/host/vm/clone",
        type : "POST",
        contentType : "application/json; charset=utf-8",
        dataType : "json",
        data: json,
        complete : function(transport) {
        	$("#btnClone").removeAttr("disabled");
            if (transport.status == 200) {
                var newVm = JSON.parse(transport.responseText);
                window.location = base + "/xen/host/vm/details/index.jsp.oo?hostId=" + hostId + "&vmId=" + newVm.data;
            } else {
                alert('Error: ' + transport.status + ", "
                                + transport.responseText);
            }
        }
	});
	return false;
}
</script>