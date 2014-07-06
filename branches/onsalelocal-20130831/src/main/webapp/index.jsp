<%@ page contentType="text/html; charset=utf-8" 
%><%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<%request.getRequestDispatcher("/ws/user/me?format=object&var=me").include(request, response);%>
<%request.getRequestDispatcher("login-check.jsp.oo").include(request, response);%>
<c:if test="${loginRequired}">
    <jsp:include page="/user/login.jsp.oo"></jsp:include>
</c:if>
<c:if test="${not loginRequired}">
    <jsp:include page="main.jsp.oo"></jsp:include>
</c:if>
