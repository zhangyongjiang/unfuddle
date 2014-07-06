<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" 
%><%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" 
%><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" 
%><%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" 
%><%
	String dataCenterId = request.getParameter("dataCenterId");
	String url = "/data-center/details?format=object&var=it&dataCenterId=" + dataCenterId;
	request.getRequestDispatcher(url).include(request, response);
%>

<div style="width:100%;">


<div class="page-header">Data Center</div>
<div style="margin:-18px 0 0px;;font-weight:normal;">${it.name}
<button style="margin-left: 6px;" onClick='window.location="<c:url value="/data-center/edit/index.jsp.oo?dataCenterId="/>${it.id}"'>Edit</button>
<button style="margin-left: 6px;" onClick="removeDataCenter()">Delete</button></div>

<p>${it.description }</p>

<c:if test="${empty it.podList }">
This data center currently has no pod. <button onClick="addPod()">Add Pod</button>
</c:if>

<c:if test="${not empty it.podList }">
<h3>PODs in this data center</h3>
<table style="border: solid 1px;">
	<tr><th>Name</th><th>Status</th><th>Gateway</th><th>DHCP</th></tr>
	<c:forEach var="pod" items="${it.podList }">
		<tr>
			<th><g:pod-link pod="${pod }"></g:pod-link></th>
			<th>${pod.allocationState }</th>
			<th>${pod.gateway }</th>
			<th>${pod.externalDhcp}</th>
		</tr>
	</c:forEach>
</table>
</c:if>

</div>

<script type="text/javascript">
function removeDataCenter() {
	var agree=confirm("Are you sure?");
	if (!agree)
		return;
    $.ajax({
        url : base + "/data-center/delete/${it.id}",
        type : "POST",
        contentType : "application/json; charset=utf-8",
        headers: {Accept : "application/json; charset=utf-8"},
        dataType : "json",
        complete : function(transport) {
                if (transport.status == 200) {
                        window.location = base + "/index.jsp.oo";
                } else {
                        alert('Error: ' + transport.status + ", "
                                        + transport.responseText);
                }
        }
	});
}

function addPod() {
	window.location = base + "/data-center/pod/create/index.jsp.oo?dataCenterId=${it.id}";
}
</script>
