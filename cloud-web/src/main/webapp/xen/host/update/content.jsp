<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
	String jobId = request.getParameter("id");
	String url = "/xen/host/details?format=object&var=it&id=" + jobId;
%>
<jsp:include page="<%=url %>"></jsp:include>

<div class="page-header">Host ${it.name} details</div>

<form method="post" onSubmit="return updateHost()">
<table style="border-left: solid 1px #fff;">
	<o:text-input-tr id="name" label="Name" value="${it.name }"></o:text-input-tr>
	<o:text-input-tr id="url" label="URL" value="${it.url }"></o:text-input-tr>
	<o:text-input-tr id="user" label="User" value="${it.user }"></o:text-input-tr>
	<o:password-input-tr id="password" label="Password" value="${it.password }"></o:password-input-tr>
	<o:submit-tr value="Update" cancel="window.history.back()"></o:submit-tr>
</table>
</form>

<script type="text/javascript">
function updateHost() {
	var req = {
		id: '${it.id}',
		name: $("#name").val(),
		url: $("#url").val(),
		user: $("#user").val(),
		password: $("#password").val()
	};
	
    var json = JSON.stringify(req);
    $.ajax({
            url : base + "/xen/host/update",
            type : "POST",
            data : json,
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            complete : function(transport) {
                    if (transport.status == 200) {
                        var host = JSON.parse(transport.responseText);
                        window.location = base + "/xen/host/details/index.jsp.oo?id=" + host.id;
                    } else {
                        alert('Error: ' + transport.status + ", "
                                        + transport.responseText);
                    }
            }
    });

    return false;
}
</script>
