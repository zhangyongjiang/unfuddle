<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<c:if test="${empty it.list}">
You have no location defined currently. 
</c:if>


<c:if test="${not empty it.list}">
<table>
<c:forEach var="loc" items="${it.list }">

<c:if test="${loc.device }">
<tr><td><input type="radio" <c:if test="${loc.current }">checked</c:if> name="location" id="loc-${loc.id}" value="${loc.id}"></td>
<td><div style="margin-left:16px;">My Current Phone Location
</div></td></tr>
</c:if>

<c:if test="${!loc.device }">
<tr><td><input type="radio" <c:if test="${loc.current }">checked</c:if> name="location" id="loc-${loc.id}" value="${loc.id}"></td>
<td><div style="margin-left:16px;">${loc.address }
</div></td></tr>
</c:if>

</c:forEach>
</table>
</c:if>

