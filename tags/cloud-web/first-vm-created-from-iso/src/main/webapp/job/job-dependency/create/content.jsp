<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<%
	String jobId = request.getParameter("jobId");
	String url = "/job/details?format=object&var=job&id=" + jobId;
%>
<jsp:include page="<%=url %>"></jsp:include>

<div class="page-header">Add dependency for job <g:job-link job="${job}"/></div>

<form method="post" onSubmit="return createJobDependency();">
	<table>
		<o:text-input-tr label="Upstream Job Id" id="upstreamJobId" ></o:text-input-tr>
		<o:text-input-tr label="Time Diff " id="timeDiff" size="80"></o:text-input-tr>
		<o:submit-tr value="Add" cancel="window.history.back()"></o:submit-tr>
	</table>
</form>

<script type="text/javascript">
function createJobDependency() {
	var req = {
		upstreamJobId: $("#upstreamJobId").val(),
		timeDiff: $("#timeDiff").val(),
		jobId: "<%=jobId%>"
	};
	
    var json = JSON.stringify(req);
    $.ajax({
            url : base + "/job/job-dependency/create",
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