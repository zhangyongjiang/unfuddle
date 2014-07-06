<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<%
String lat = request.getParameter("lat");
String lng = request.getParameter("lng");
String location = "/user/search?format=object&var=it";
String mapurl = "/index.jsp.oo";
if(lat!=null && lng!=null) {
    location = location + "&lat=" + lat + "&lng=" + lng;
    mapurl = "/index.jsp.oo?#center=" + lat + "," + lng; 
}
%>

<h3>Nearby Friends</h3>
<jsp:include page="<%=location%>"></jsp:include>
<!--div style='clear:both;'>
<select name="miles" id="miles" >
	<option value="1" <c:if test="${it.miles <1.001 }">selected</c:if>>in 1 mile</option>
	<option value="15" <c:if test="${it.miles > 1.001 && it.miles <15.001 }">selected</c:if>>in 15 miles</option>
	<option value="30" <c:if test="${it.miles > 15.001 && it.miles <30.001 }">selected</c:if>>in 30 miles</option>
	<option value="50" <c:if test="${it.miles > 30.001 && it.miles <50.001 }">selected</c:if>>in 50 miles</option>
	<option value="100" <c:if test="${it.miles >50 }">selected</c:if>>in 100 miles</option>
</select>
</div-->

<div id="searchResult">

<c:if test="${empty it.list}">
No Match Found. 
</c:if>

<c:if test="${not empty it.list}">
<div style="margin-top:-50px;float:right;" data-inline="true">
<a href="#" data-theme="d" data-role='button' onclick="window.location='<c:url value="/m/map"/><%=mapurl%>'">Map</a>
</div>
</c:if>

<c:forEach var="user" items="${it.list }">
	<div style='width:100%;clear:both;padding:6px;border-bottom:solid 1px #aaa;'>
		<div style='float:left;margin-right:6px;'><a href='#' onclick='window.location="<c:url value="/m/dating/profile/index.jsp.oo"/>?uid=${user.id}"'><img width="60" src='<g:user-icon user="${user}"/>'/></a></div>
		<a href='#' onclick='window.location="<c:url value="/m/dating/profile/index.jsp.oo"/>?uid=${user.id}"'>${user.name}</a>
		<div>
			<div style='font-size:0.9em;color:#666;margin-top:6px;'>
			<span>${user.gender}, ${user.age} years old</span>
			</div>
			${user.interests}
			<c:if test="${not empty user.locationDistance }">
			<div style="margin-top:6px;color:#666;font-size:0.8em;font-weight:normal;">${user.locationDistance.city}, ${user.locationDistance.state }, <fmt:formatNumber type="number" maxFractionDigits="1" value="${user.locationDistance.distance}" /> mile</div>
			</c:if>
			<span style="color:#aaa;"> </span>
		</div>
		<div style="width:100%;clear:both;height:1px;">&nbsp;</div>
	</div>
</c:forEach>

</div>

<script type="text/javascript">
function sendMessage(userId) {
    window.location = '<c:url value="/m/message/send/index.jsp.oo?to="/>' + userId;
}
</script>
