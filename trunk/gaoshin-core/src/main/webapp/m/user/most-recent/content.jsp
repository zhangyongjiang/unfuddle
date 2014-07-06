<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<jsp:include page="/user/most-recents?format=object&var=recents"></jsp:include>
<h3>New Arrived Friends</h3>

<c:forEach var="user" items="${recents.list }">
	<div style='width:100%;clear:both;padding:6px;border-bottom:solid 1px #aaa;'>
		<div style='float:left;margin-right:6px;'><img onclick='window.location="<c:url value="/m/dating/profile/index.jsp.oo"/>?uid=${user.id}"' width="60" src='<g:user-icon user="${user}"/>'/></div>
		<a href='#' onclick='window.location="<c:url value="/m/dating/profile/index.jsp.oo"/>?uid=${user.id}"'>${user.name}</a>
		<div>
			${fn:replace(user.interests, newLineChar, ", ")}

			<c:if test="${not empty user.currentLocation }">
			<div style="margin-top:6px;color:#666;font-size:0.8em;font-weight:normal;">${user.currentLocation.city}, ${user.currentLocation.state }</div>
			</c:if>
			
			<span style="color:#aaa;">Arrived @ <o:millisecond-to-date format="yyyy/MM/dd HH:mm" time="${user.regtime.time.time }"></o:millisecond-to-date> </span>
		</div>
		<div style="width:100%;clear:both;height:1px;">&nbsp;</div>
	</div>
</c:forEach>

<script type="text/javascript">
function sendMessage(userId) {
    window.location = '<c:url value="/m/message/send/index.jsp.oo?to="/>' + userId;
}
</script>