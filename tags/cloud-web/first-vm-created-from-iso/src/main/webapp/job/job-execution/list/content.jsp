<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
	String jobId = request.getParameter("jobId");
	String url = "/job/job-execution/list?format=object&var=it&jobId=" + jobId;
%>
<jsp:include page="<%=url %>"></jsp:include>

<div class="page-header">Job executions for <g:job-link job="${it.job}"/></div>

<c:if test="${empty it.items }">
	No job execution found.
</c:if>

<c:if test="${not empty it.items }">
	<table style="border:solid 1px;">
		<tr><th>Execution ID</th><th>Status</th><th>Scheduled Start Time</th><th>Start Time</th><th>Expected Duration</th><th>Actual Duration</th></tr>
		<c:forEach var="jobexec" items="${it.items }">
		
			<c:set var="color">#ddd</c:set>
			<c:if test="${jobexec.status == 'Failed'}"><c:set var="color">red</c:set></c:if>
			
			<c:set var="slaColor">#ddd</c:set>
			<c:if test="${jobexec.expectedDuration > 0 && jobexec.expectedDuration < jobexec.duration}"><c:set var="slaColor">red</c:set></c:if>
			
			<tr>
				<td style="text-align:left;border:solid 1px #ddd;padding:6px;"><a href="<c:url value="/job/job-execution/details/index.jsp.oo?jobExecutionId=${jobexec.id}"/>">${jobexec.id}</a></td>
				<!-- td style="text-align:left;border:solid 1px #ddd;padding:6px;"><a style="text-decoration:none;" href="<c:url value="/job/details/index.jsp.oo?id=${jobexec.jobId}"/>">${jobexec.job.name}</a></td-->
				<td style="text-align:left;border:solid 1px #ddd;padding:6px;"><img border="0" src="<c:url value="/images/status-${jobexec.status}.gif"/>"/> ${jobexec.status}</td>
				<td style="text-align:left;border:solid 1px #ddd;padding:6px;"><o:millisecond-to-date format="yyyy-MM-dd HH:mm:ss" time="${jobexec.scheduledStartTime}"/>&nbsp;</td>
				<td style="text-align:left;border:solid 1px #ddd;padding:6px;"><o:millisecond-to-date format="yyyy-MM-dd HH:mm:ss" time="${jobexec.startTime}"/>&nbsp;</td>
				<td style="text-align:left;border:solid 1px #ddd;padding:6px;">${jobexec.expectedDuration}</td>
				<td style="text-align:left;border:solid 1px ${slaColor};padding:6px;"><o:millisecond-to-time-len time="${jobexec.duration}"/>&nbsp;</td>
			</tr>
		</c:forEach>
	</table>
</c:if>

<script type="text/javascript">
</script>
