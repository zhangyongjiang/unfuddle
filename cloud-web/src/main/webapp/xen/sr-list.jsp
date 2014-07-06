<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
	String hostId = request.getParameter("hostId");
	String url = "/xen/host/sr-list?format=object&var=srList&hostId=" + hostId;
	request.getRequestDispatcher(url).include(request, response);
%>
<div id="sr">
	<c:if test="${empty srList.items }">
	<div style="margin: 20px;">No storage found on this host</div>
	</c:if>
	
	<c:if test="${not empty srList.items }">
		<select id="srList">
			<c:forEach var="sr" items="${srList.items}">
				<option value="${sr.uuid }">${sr.name}</option>
			</c:forEach>
		</select>
	</c:if>
</div>
