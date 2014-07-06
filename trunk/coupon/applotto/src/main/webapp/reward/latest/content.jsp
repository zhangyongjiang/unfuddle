<%@page import="java.net.URLEncoder"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%
	String url = "/ws/user/application/reward/latest?format=object&" + request.getQueryString();
	request.getRequestDispatcher(url).include(request, response);
%>

<h1>Latest Rewards</h1>

<c:if test="${empty it.items}">
	Currently no rewards.
</c:if>

<c:if test="${not empty it.items}">
    <table style="border: solid 1px;">
        <tr>
            <th style="border:solid 1px #ccc; padding:6px;">Activity</th>
            <th style="border:solid 1px #ccc; padding:6px;">Points</th>
            <th style="border:solid 1px #ccc; padding:6px;">Application</th>
            <th style="border:solid 1px #ccc; padding:6px;">Market</th>
            <th style="border:solid 1px #ccc; padding:6px;">About Application</th>
            <th style="border:solid 1px #ccc; padding:6px;">User</th>
        </tr>
	<c:forEach var="reward" items="${it.items}">
        <tr>
            <td style="border:solid 1px #ccc; padding:6px;">${reward.type }</td>
            <td style="border:solid 1px #ccc; padding:6px;">${reward.points }</td>
            <td style="border:solid 1px #ccc; padding:6px;">
                <div>${reward.applicationDetails.name}</div>
                <div><img width="48" src='${reward.applicationDetails.icon}'/></div></td>
            <td style="border:solid 1px #ccc; padding:6px;">${reward.applicationDetails.type}</td>
            <td style="border:solid 1px #ccc; padding:6px;">${fn:substring(reward.applicationDetails.description, 0, 300)} ... </td>
            <td valign="middle" style="border:solid 1px #ccc; padding:6px;">
                <div style="float:left;margin-top:16px;"><a href='<c:url value="/user/index.jsp.oo?userId="/>${reward.applicationDetails.userId}'>${reward.applicationDetails.user.name}</a></div><div style="float:left;margin-left:10px;"><img width="48" src='${reward.applicationDetails.user.icon}'/></div>
            </td>
        </tr>
	</c:forEach>
    </table>
</c:if>

