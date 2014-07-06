<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<div class="page-header">Create a Work Flow 创建工作流程</div>

<form method="post" action='<c:url value="/jobcreate"/>' onSubmit="return createJob()">
<table style="border-left: solid 1px #fff;">
	<o:text-input-tr id="name" label="Name"></o:text-input-tr>
	<o:text-input-tr id="cronExpression" label="Cron Expression"></o:text-input-tr>
	<o:text-input-tr id="retention" label="Retention in Days"></o:text-input-tr>
	<o:text-input-tr id="enabled" label="Enabled" value="false"></o:text-input-tr>
	<o:text-input-tr id="expectedDuration" label="Expected Duration "></o:text-input-tr>
	<o:text-input-tr id="notificationEmail" label="Error Notication Email"></o:text-input-tr>
	<o:textarea-tr   id="description" label="Description"></o:textarea-tr>
	<o:submit-tr value="Create" cancel="window.history.back()"></o:submit-tr>
</table>
</form>

<jsp:include page="cron-format.html.oo"></jsp:include>

<script type="text/javascript">
function createJob() {
	var req = {
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
            url : base + "/job/create",
            type : "POST",
            data : json,
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            complete : function(transport) {
                    if (transport.status == 200) {
                            window.location = base + "/job/list/index.jsp.oo";
                    } else {
                            alert('Error: ' + transport.status + ", "
                                            + transport.responseText);
                    }
            }
    });

    return false;
}
</script>

