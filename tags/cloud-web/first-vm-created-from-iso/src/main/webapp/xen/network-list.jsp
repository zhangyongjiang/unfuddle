<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
	String hostId = request.getParameter("hostId");
	String url = "/xen/host/network-list?format=object&var=networkList&hostId=" + hostId;
	request.getRequestDispatcher(url).include(request, response);
%>
<div id="network">
	<c:if test="${empty networkList.items }">
	<div style="margin: 20px;">No network found on this host</div>
	</c:if>
	
	<c:if test="${not empty networkList.items }">
		<select id="networkList">
			<c:forEach var="nw" items="${networkList.items}">
				<option value="${nw.uuid }">${nw.nameLabel}</option>
			</c:forEach>
		</select>
	</c:if>
</div>
