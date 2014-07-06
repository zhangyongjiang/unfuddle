<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<%
	String id = request.getParameter("id");
	String url = "/job/details?format=object&var=job&id=" + id;
%>
<jsp:include page="<%=url %>"></jsp:include>

<div class="page-header">Edit job</div>

<form method="post" onSubmit="return editJob()">
<table style="border-left: solid 1px #fff;">
	<o:text-input-tr id="name" label="Name" value="${job.name }"></o:text-input-tr>
	<o:text-input-tr id="cronExpression" label="Cron Expression" value="${job.cronExpression }"></o:text-input-tr>
	<o:text-input-tr id="retention" label="Retention in Days" value="${job.retention }"></o:text-input-tr>
	<o:text-input-tr id="enabled" label="Enabled" value="${job.enabled }"></o:text-input-tr>
	<o:text-input-tr id="expectedDuration" label="Expected Duration " value="${job.expectedDuration }"></o:text-input-tr>
	<o:text-input-tr id="notificationEmail" label="Error Notication Email" value="${notificationEmail }"></o:text-input-tr>
	<o:textarea-tr   id="description" label="Description">${job.description }</o:textarea-tr>
	<o:submit-tr value="Update" cancel="window.history.back()"></o:submit-tr>
</table>
</form>

<jsp:include page="cron-format.html.oo"></jsp:include>

<script type="text/javascript">
function editJob() {
	var req = {
		id: '${job.id}',
		name: $("#name").val(),
		cronExpression: $("#cronExpression").val(),
		retention: $("#retention").val(),
		enabled: $("#enabled").val(),
		expectedDuration: $("#expectedDuration").val(),
		notificationEmail: $("#notificationEmail").val(),
		description: $("#description").val()
	};
    var json = JSON.stringify(req);
    
    $.ajax({
            url : base + "/job/update",
            type : "POST",
            data : json,
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            complete : function(transport) {
                    if (transport.status == 200) {
                            window.location = base + "/job/details/index.jsp.oo?id=${job.id}";
                    } else {
                            alert('Error: ' + transport.status + ", "
                                            + transport.responseText);
                    }
            }
    });

    return false;
}
</script>

