<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
	String jobId = request.getParameter("id");
	String url = "/job/details?format=object&var=it&id=" + jobId;
%>
<jsp:include page="<%=url %>"></jsp:include>

<div class="page-header">Work Flow <span style="font-weight:normal;font-size:0.8em;">${it.name}</span></div>

<div>
	<table style="border:solid 1px;width:800px;"><tbody>
	<o:tr-label-value label="ID" >
		${it.id }
        <div style="float:right;">
        	<a href="#" class="undera" onClick="deleteJob('${it.id}')">Delete</a>
        </div>
        
		<div style="float:right;margin-right:16px;"><a class="undera" href="#" onClick="runNow('${it.id}')">Run Now</a></div>
		<div style="float:right;margin-right:16px;"><a class="undera" href="<c:url value="/job/job-execution/list/index.jsp.oo?jobId=${it.id}"/>">Job Executions</a></div>
		
		<div style="float:right;margin-right:16px;">
			<a class="undera" href="<c:url value="/job/edit/index.jsp.oo?id=${it.id}"/>">Edit</a>
		</div>
		
		<div style="float:right;margin-right:16px;">
			 <a href="#" class="undera" onClick="enableJob('${it.id}', !${it.enabled})"><c:if test="${it.enabled}">Disable</c:if><c:if test="${it.enabled == false}">Enable</c:if></a>
		</div>
	</o:tr-label-value>
	
	<o:tr-label-value label="Name" >${it.name}</o:tr-label-value>
	<o:tr-label-value label="Description" >${it.description}</o:tr-label-value>
	
	<o:tr-label-value label="Cron Expression" >${it.cronExpression}</o:tr-label-value>
	<o:tr-label-value label="Retention Days" >${it.retention}</o:tr-label-value>
	<o:tr-label-value label="Expected Duration " >${it.expectedDuration}</o:tr-label-value>
	<o:tr-label-value label="Error Notification Email" >${it.notificationEmail}</o:tr-label-value>

	<c:set var="addJobConfUrl"><a href='<c:url value="/job/job-conf/create/index.jsp.oo?jobId=${it.id}"/>'>Add</a></c:set>
	<o:tr-label-value label="Job Configuration<br/><br/>${addJobConfUrl}" >
<div style="float:left;">
		<table style="border:solid 1px blue;margin-bottom:16px;">
			<c:forEach var="conf" items="${it.jobConfList.list}">
				<o:tr-label-value label="${conf.ckey}" >
					<a href='<c:url value="/job/job-conf/edit/index.jsp.oo?id=${conf.id}"/>'>${conf.cvalue}</a> 
					<div style="float:right;margin-left:40px;">
						<a href="#" onClick='deleteJobConf("${conf.id}")'><img src='<c:url value="/images/delete_icon.gif"/>' border="0"/></a>
					</div>
				</o:tr-label-value>
			</c:forEach>
		</table>
</div>
<div style="float:right;">		
		Predefined configuration examples:
		<pre>
	yyyyMMdd	20100410
	year		2010
	month		12
	day		31
	hour		17
	minute		36
	second		59
		</pre>
</div>
	</o:tr-label-value>

	<c:set var="addTaskUrl"><a href='<c:url value="/job/task/create/index.jsp.oo?jobId=${it.id}"/>'>Add</a></c:set>
	<o:tr-label-value label="Tasks<br/></br>${addTaskUrl}" >
			<c:forEach var="task" items="${it.taskList.list}">
				<c:set var="color"><c:if test="${task.errorHandlingTask}">red</c:if><c:if test="${task.errorHandlingTask == false}">blue</c:if></c:set>
				<table style="width:100%;border:solid 1px ${color};margin-bottom:16px;">
					<o:tr-label-value label="Task ID" >
						<a href='<c:url value="/job/task/edit/index.jsp.oo?id="/>${task.id}'>${task.id}</a>
						<div style="float:right;"><a href='#' onClick='deleteTask("${task.id}")'><img src='<c:url value="/images/delete_icon.gif"/>' border="0"/></a></div>
					</o:tr-label-value>
					<o:tr-label-value label="Exec Order" >${task.execOrder}</o:tr-label-value>
					<o:tr-label-value label="Name" >${task.name}</o:tr-label-value>
					<o:tr-label-value label="Disabled?" >${task.disabled}</o:tr-label-value>
					<o:tr-label-value label="Command" >${fn:escapeXml(task.args)}</o:tr-label-value>
					<o:tr-label-value label="Handler" >${task.handler}</o:tr-label-value>
					<o:tr-label-value label="Num of Retries" >${task.numOfRetries}</o:tr-label-value>
					<o:tr-label-value label="Expected Duration" >${task.expectedDuration} second(s)</o:tr-label-value>
					<o:tr-label-value label="Retry Interval" >${task.retryInterval} second(s)</o:tr-label-value>
					<o:tr-label-value label="Is Error Handling Task?" >${task.errorHandlingTask}</o:tr-label-value>
					<o:tr-label-value label="Description" >${task.description}</o:tr-label-value>
				</table>
			</c:forEach>
	</o:tr-label-value>

	<c:set var="addDependentUrl"><a href='<c:url value="/job/job-dependency/create/index.jsp.oo?jobId=${it.id}"/>'>Add</a></c:set>
	<o:tr-label-value label="Dependents<br/></br>${addDependentUrl}" >
			<c:forEach var="dependent" items="${it.jobDependencyList.list}">
				<table style="border:solid 1px blue;margin-bottom:16px;">
					<o:tr-label-value label="Dependent ID" >
						<a href='<c:url value="/job/job-dependency/edit/index.jsp.oo?id="/>${dependent.id}'>${dependent.id}</a>
						<div style="float:right;margin-left:48px;"><a href='#' onClick='deleteJobDependency("${dependent.id}")'><img src='<c:url value="/images/delete_icon.gif"/>' border="0"/></a></div>
					</o:tr-label-value>
					<o:tr-label-value label="Up Stream" >${dependent.blockedByJob.id} ${dependent.blockedByJob.name} </o:tr-label-value>
					<o:tr-label-value label="Time Diff Seconds" >${dependent.timeDiff}</o:tr-label-value>
				</table>
			</c:forEach>
	</o:tr-label-value>

</tbody></table></div>

<script type="text/javascript">
function deleteJob(jobId) {
	var agree=confirm("Are you sure you wish to continue?");
	if (!agree)
		return;
    $.ajax({
        url : base + "/job/delete/" + jobId,
        type : "POST",
        contentType : "application/json; charset=utf-8",
        headers: {Accept : "application/json; charset=utf-8"},
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
}

function enableJob(jobId, enable) {
	var agree=confirm("Are you sure you wish to continue?");
	if (!agree)
		return;
    $.ajax({
        url : base + "/job/enable/" + jobId + "/" + enable,
        type : "POST",
        contentType : "application/json; charset=utf-8",
        dataType : "json",
        complete : function(transport) {
                if (transport.status == 200) {
                        window.location = base + "/job/details/index.jsp.oo?id=" + jobId;
                } else {
                        alert('Error: ' + transport.status + ", "
                                        + transport.responseText);
                }
        }
	});
}

function deleteJobConf(jobConfId) {
	var agree=confirm("Are you sure you wish to continue?");
	if (!agree)
		return;
    $.ajax({
        url : base + "/job/job-conf/delete/" + jobConfId,
        type : "POST",
        contentType : "application/json; charset=utf-8",
        dataType : "json",
        complete : function(transport) {
                if (transport.status == 200) {
                        window.location = base + "/job/details/index.jsp.oo?id=${it.id}";
                } else {
                        alert('Error: ' + transport.status + ", "
                                        + transport.responseText);
                }
        }
	});
}

function deleteTask(taskId) {
	var agree=confirm("Are you sure you wish to continue?");
	if (!agree)
		return;
    $.ajax({
        url : base + "/job/task/delete/" + taskId,
        type : "POST",
        contentType : "application/json; charset=utf-8",
        dataType : "json",
        complete : function(transport) {
                if (transport.status == 200) {
                        window.location = base + "/job/details/index.jsp.oo?id=${it.id}";
                } else {
                        alert('Error: ' + transport.status + ", "
                                        + transport.responseText);
                }
        }
	});
}

function deleteJobDependency(jobDependencyId) {
	var agree=confirm("Are you sure you wish to continue?");
	if (!agree)
		return;
    $.ajax({
        url : base + "/job/job-dependency/delete/" + jobDependencyId,
        type : "POST",
        contentType : "application/json; charset=utf-8",
        dataType : "json",
        complete : function(transport) {
                if (transport.status == 200) {
                        window.location = base + "/job/details/index.jsp.oo?id=${it.id}";
                } else {
                        alert('Error: ' + transport.status + ", "
                                        + transport.responseText);
                }
        }
	});
}

function runNow(jobId) {
	var agree=confirm("Are you sure you wish to continue?");
	if (!agree)
		return;
	var req = {
	};
    var json = JSON.stringify(req);
    
    $.ajax({
        url : base + "/job/run/" + jobId,
        type : "POST",
        contentType : "application/json; charset=utf-8",
        data: json,
        dataType : "json",
        complete : function(transport) {
                if (transport.status == 200) {
                        window.location = base + "/job/job-execution/list/index.jsp.oo?jobId=" + jobId;
                } else {
                        alert('Error: ' + transport.status + ", "
                                        + transport.responseText);
                }
        }
	});
}
</script>
