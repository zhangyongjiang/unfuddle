<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<c:forEach var="node" items="${it.list}">
	<c:if test="${node.name != null && node.name != 'null' }">
		<div style="float:left;width:300px;">
			<a href="/aws/node/${node.id }">${node.name}</a>
			<a href="javascript:void(0)" onclick="parent.gaoshin.addChild('${node.name}')"><img src='<c:url value="/m/images/right.png"/>'/></a>
		</div>
	</c:if>
</c:forEach>