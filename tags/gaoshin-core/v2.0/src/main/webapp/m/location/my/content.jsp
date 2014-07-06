<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<div style="float:right;"><a href='<c:url value="/m/location/new/index.jsp.oo"/>'>Add one</a></div>

<c:if test="${empty it.list}">
You have no location defined currently. 
</c:if>


<c:if test="${not empty it.list}">
<ul>
<c:forEach var="loc" items="${it.list }">
<li>${loc.address } ${loc.firstUpdate.time } ${loc.lastUpdate.time } device: ${loc.device }</li>
</c:forEach>
</ul>
</c:if>