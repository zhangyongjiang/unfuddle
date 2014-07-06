<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
	String dataCenterId = request.getParameter("dataCenterId");
	String url = "/data-center/details?format=object&var=it&dataCenterId=" + dataCenterId;
%>
<jsp:include page="<%=url %>"></jsp:include>

<div class="page-header">Data Center 计算中心 <span style="font-weight:normal;font-size:0.8em;">${it.name}</span></div>

<p>${it.description }</p>

<c:if test="${empty it.podList }">
This data center currently has no pod. <button>Add Pod</button>
</c:if>

<c:if test="${it.podList }">
<h3>PODs in this data center</h3>
<table style="border: solid 1px;">
	<tr><th>Name</th><th>Status</th><th>Gateway</th><th>DHCP</th></tr>
	<c:forEach var="pod" items="${it.podList }">
		<tr>
			<th>${pod.name }</th>
			<th>${pod.allocationState }</th>
			<th>${pod.gateway }</th>
			<th>${pod.externalDhcp}</th>
		</tr>
	</c:forEach>
</table>
</c:if>

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
