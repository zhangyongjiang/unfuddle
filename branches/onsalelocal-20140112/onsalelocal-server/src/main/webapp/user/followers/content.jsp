<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>

<%
    String param = request.getQueryString();
    String url = "/ws/v2/user/followers?format=object&" + param;
    request.getRequestDispatcher(url).include(request, response);
%>

<osl:ws path="<%=url %>"/>
<h3>${it.user.firstName } ${it.user.lastName }'s Followers</h3>

<c:if test="${empty it.items }">
    No follower found.
</c:if>  

<c:if test="${not empty it.items }">
<ul>
<c:forEach var="f" items="${it.items }">
    <li><a href='<c:url value="/user/profile/index.jsp.oo?userId="/>${f.user.id}'>${f.user.firstName } ${f.user.lastName }</a></li>
</c:forEach>
</ul>
</c:if>  

<script type="text/javascript">
$(document).ready(function(){
});
</script>
