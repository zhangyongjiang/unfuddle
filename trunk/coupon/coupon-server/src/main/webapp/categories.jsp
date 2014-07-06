<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<% String cat = request.getParameter("category"); request.setAttribute("pcat", cat); %>
<jsp:include page="/ws/category/tops?format=object&var=cats"></jsp:include>
<select name="category" id="category">
	<option value="" >ANY</option>
	<c:forEach var="cat" items="${cats.items }">
		<option value="${cat.name}" <c:if test="${cat.name == pcat }">selected</c:if>>${cat.name }</option>
	</c:forEach>
</select>