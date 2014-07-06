<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="select" %>
<jsp:include page="/job/task/type-list?format=object&var=typeList"></jsp:include>
<select name="handler" id="handler">
<c:forEach var="taskType" items="${typeList.list }">
	<c:if test="${taskType == select }">
		<option selected value="${taskType }">${taskType }</option>
	</c:if>
	<c:if test="${taskType != select }">
		<option value="${taskType }">${taskType }</option>
	</c:if>
</c:forEach>
</select>