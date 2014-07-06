<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<jsp:include page="/user/current?format=object&var=me"></jsp:include>

<c:if test="${empty me.id}">
	<jsp:include page="home.jsp.oo"></jsp:include>
</c:if>
 
<c:if test="${not empty me.id}">
	<jsp:include page="/m/dating/profile/index.jsp.oo"></jsp:include>
</c:if>
