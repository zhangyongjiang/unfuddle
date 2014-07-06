<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
	String jobExecutionId = request.getParameter("jobExecutionId");
	String url = "/job/job-execution/details?format=object&var=it&jobExecutionId=" + jobExecutionId;
	request.getRequestDispatcher(url).include(request, response);
%>

<div class="page-header">Job execution ${it.id} for job <g:job-link job="${it.job }"></g:job-link></div>

<c:set var="color">#ccc</c:set>
<c:if test="${it.status == 'Failed'}"><c:set var="color">red</c:set></c:if>

<table style="border:solid 1px;min-width:720px;"><tbody>
	<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="ID" >
		${it.id}
	</o:tr-label-value>
	<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Job" >
		<g:job-link job="${it.job }"></g:job-link>
		<a style="margin-left:20px;" href="<c:url value="/job/job-execution/list/index.jsp.oo?jobId=${it.jobId}"/>">All Job Executions</a>
		<br/>
		${it.note}
	</o:tr-label-value>
	<o:tr-label-value tdstyle="text-align:left;border:solid 1px ${color};padding:6px;" label="Status" >
		<div style="float:left;"><img border="0" src="<c:url value="/images/status-${it.status}.gif"/>"/> ${it.status}</div>
	</o:tr-label-value>
	<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Expected Start Time" ><o:millisecond-to-date format="yyyy-MM-dd HH:mm:ss" time="${it.scheduledStartTime}"/></o:tr-label-value>
	<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Expected Duration" ><o:millisecond-to-time-len time="${it.expectedDuration}"/></o:tr-label-value>
	<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Actual Start Time" ><o:millisecond-to-date format="yyyy-MM-dd HH:mm:ss" time="${it.startTime}"/></o:tr-label-value>
	<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Actual Duration" ><o:millisecond-to-time-len time="${it.duration}"/></o:tr-label-value>
	<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Error Notification Email" >${it.notificationEmail}</o:tr-label-value>

	<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Up Streams">
		<jsp:include page="/job/upstreams/${it.id}">
			<jsp:param value="simple" name="view"/>
		</jsp:include>
	</o:tr-label-value>
	
	<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Down Streams">
		<jsp:include page="/job/downstreams/${it.id}">
			<jsp:param value="simple" name="view"/>
		</jsp:include>
	</o:tr-label-value>
	
	<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Tasks" >
			<c:forEach var="task" items="${it.children}">
				<table style="border:solid 1px blue;margin-bottom:16px;width:100%;">
					<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Task" >${task.name} : ${task.id}</o:tr-label-value>
					<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Status" ><img border="0" src="<c:url value="/images/status-${task.status}.gif"/>"/> ${task.status}</o:tr-label-value>
					<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Start Time" ><o:millisecond-to-date format="yyyy-MM-dd HH:mm:ss" time="${task.startTime}"/></o:tr-label-value>
					<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Command" >${fn:escapeXml(task.args)}</o:tr-label-value>
					<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Expected Duration" ><o:millisecond-to-time-len time="${task.expectedDuration}"/></o:tr-label-value>
					<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Duration" ><o:millisecond-to-time-len time="${task.duration}"/></o:tr-label-value>
					<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Tried Times" >${task.actualNumOfTries }</o:tr-label-value>
					<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="Output" >
				            <pre>${task.logMsg}</pre>
					</o:tr-label-value>
				</table>
			</c:forEach>
	</o:tr-label-value>

</tbody></table>

