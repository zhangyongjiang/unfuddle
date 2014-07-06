<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
	String hostId = request.getParameter("hostId");
	String srUuid = request.getParameter("srUuid");
	String url = "/xen/host/sr/details?hostId=" + hostId + "&srUuid=" + srUuid + "&format=object&var=it";
	out.write("<!-- " + url + "-->");
%>
<jsp:include page="<%=url %>"></jsp:include>

<div class="page-header">Storage Repository Details</div>

<table class="detail-table" style="border: solid 1px;">
	<tr>
	<tr><th>Host</th>
	<td><a href='<c:url value="/xen/host/details/index.jsp.oo?hostId="/>${it.hostId}'>${it.host.name}</a></td></tr>
	<tr><th>Name</th>
	<td>${it.name}</td></tr>

	<tr><th>Description</th>
	<td>${it.description}</td></tr>

	<tr><th>UUID</th>
	<td>${it.uuid}</td></tr>

	<tr><th>Type</th>
	<td>${it.type}</td></tr>

	<tr><th>Physical Size</th>
	<td>${it.physicalSize}</td></tr>

	<tr><th>Physical Utilization</th>
	<td>${it.physicalUtilisation}</td></tr>
</table>

<h3 style="margin-top: 32px;">Virtual Device Images</h3>
<c:if test="${empty it.vdiList.items }">
<div style="margin: 20px;">No Virtual Device Image found on this host</div>
</c:if>

<c:if test="${not empty it.vdiList.items }">
<table style="border: solid 1px;">
<tr>
	<th>Name Label</th>
	<th>Type</th>
	<th>Location</th>
	<th>Description</th>
</tr>
<c:forEach var="vdi" items="${it.vdiList.items}">
<tr>
	<td>${vdi.nameLabel}</td>
	<td>${vdi.type}</td>
	<td>${vdi.location}</td>
	<td>${vdi.nameDescription}&nbsp;</td>
</tr>
</c:forEach>
</table>
</c:if>

<script type="text/javascript">
</script>