<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<%
	String id = request.getParameter("id");
	String url = "/job/task/details?format=object&var=it&id=" + id;
%>
<jsp:include page="<%=url %>"></jsp:include>

<div class="page-header">Edit task for job <g:job-link job="${it.job}"/></div>

<form method="post" onSubmit="return updateTask();">
	<table>
		<o:text-input-tr label="Name" id="name" value="${it.name}"></o:text-input-tr>
		<o:text-input-tr label="Arguments" id="args" size="80" value="${it.args}"></o:text-input-tr>
		<o:text-input-tr label="Is Error Handling Task?" id="errorHandlingTask" value="${it.errorHandlingTask}"></o:text-input-tr>
		<o:text-input-tr label="Exec Order" id="execOrder"  value="${it.execOrder}"></o:text-input-tr>
		<o:text-input-tr label="Disabled?" id="disabled"  value="${it.disabled}"></o:text-input-tr>
		<o:text-input-tr label="Expected Duration " id="expectedDuration" value="${it.expectedDuration}"></o:text-input-tr>
		<o:text-input-tr label="Handler" id="handler" value="${it.handler}"></o:text-input-tr>
		<o:text-input-tr label="Num Of Retries" id="numOfRetries" value="${it.numOfRetries}"></o:text-input-tr>
		<o:text-input-tr label="Retry Interval " id="retryInterval" value="${it.retryInterval}"></o:text-input-tr>
		<o:textarea-tr   id="description" label="Description">${it.description }</o:textarea-tr>
		<o:submit-tr value="Update" cancel="window.history.back()"></o:submit-tr>
	</table>
</form>

<script type="text/javascript">
function updateTask() {
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
		id: "<%=id%>",
		jobId: "${it.jobId}"
	};
	
    var json = JSON.stringify(req);
    $.ajax({
            url : base + "/job/task/update",
            type : "POST",
            data : json,
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            complete : function(transport) {
                    if (transport.status == 200) {
                            window.location = base + "/job/details/index.jsp.oo?id=${it.jobId}";
                    } else {
                            alert('Error: ' + transport.status + ", "
                                            + transport.responseText);
                    }
            }
    });

    return false;
}
</script>