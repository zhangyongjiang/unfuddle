<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
	String hostId = request.getParameter("hostId");
	String url = "/xen/host/vdi-list?format=object&var=vdiList&hostId=" + hostId;
	request.getRequestDispatcher(url).include(request, response);
%>
<div id="vdi">
	<c:if test="${empty vdiList.items }">
	<div style="margin: 20px;">No Virtual Device Image found on this host</div>
	</c:if>
	
	<c:if test="${not empty vdiList.items }">
		<select id="vdiList">
			<c:forEach var="vdi" items="${vdiList.items}">
				<option value="${vdi.uuid }">${vdi.nameLabel}</option>
			</c:forEach>
		</select>
	</c:if>
</div>
