<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<%
	String jobId = request.getParameter("jobId");
	String url = "/job/details?format=object&var=job&id=" + jobId;
%>
<jsp:include page="<%=url %>"></jsp:include>

<div class="page-header">Add task for job <g:job-link job="${job}"/></div>

<form method="post" onSubmit="return createTask();">
	<table>
		<o:text-input-tr label="Name" id="name"></o:text-input-tr>
		<o:text-input-tr label="Arguments" id="args" size="80"></o:text-input-tr>
		<o:text-input-tr label="Is Error Handling Task?" id="errorHandlingTask" value="false"></o:text-input-tr>
		<o:text-input-tr label="Exec Order" id="execOrder" ></o:text-input-tr>
		<o:text-input-tr label="Disabled?" id="disabled" ></o:text-input-tr>
		<o:text-input-tr label="Expected Duration " id="expectedDuration"></o:text-input-tr>
		<o:tr label="Task Type" ><g:task-types/></o:tr>
		<o:text-input-tr label="Num Of Retries" id="numOfRetries"></o:text-input-tr>
		<o:text-input-tr label="Retry Interval " id="retryInterval"></o:text-input-tr>
		<o:textarea-tr   id="description" label="Description"></o:textarea-tr>
		<o:submit-tr value="Add" cancel="window.history.back()"></o:submit-tr>
	</table>
</form>

<script type="text/javascript">
function createTask() {
	var req = {
		name: $("#name").val(),
		args: $("#args").val(),
		errorHandlingTask: $("#errorHandlingTask").val(),
		execOrder: $("#execOrder").val(),
		disabled: $("#disabled").val(),
		expectedDuration: $("#expectedDuration").val(),
		handler: $("#handler").val(),
		numOfRetries: $("#numOfRetries").val(),
		retryInterval: $("#retryInterval").val(),
		description: $("#description").val(),
		jobId: "<%=jobId%>"
	};
	
    var json = JSON.stringify(req);
    $.ajax({
            url : base + "/job/task/create",
            type : "POST",
            data : json,
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            complete : function(transport) {
                    if (transport.status == 200) {
                            window.location = base + "/job/details/index.jsp.oo?id=<%=jobId%>";
                    } else {
                            alert('Error: ' + transport.status + ", "
                                            + transport.responseText);
                    }
            }
    });

    return false;
}
</script>