<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>

<%
    String param = request.getQueryString();
    String url = "/ws/user/notifications?format=object&" + param;
    request.getRequestDispatcher(url).include(request, response);
%>

<osl:ws path="<%=url %>"/>
<h3>Notifications</h3>

<c:if test="${empty it.items }">
    No notification found.
</c:if>

<c:forEach var="noti" items="${it.items }">
    <div style="margin:6px;padding:6px;border:solid 1px #bbb;">
        <osl:User user="${noti.user }"/> ${noti.type}
        ${noti.alert} 
        <span style="color:#ccc;font-size:0.8em;"><o:millisecond-to-date time="${noti.created }"></o:millisecond-to-date></span>
        <c:choose>
            <c:when test="${noti.type == 'CommentOffer' }">
                <a href='<c:url value="/offer/details/index.jsp.oo?offerId="/>${noti.offer.id}'><img src='${noti.offer.largeImg }' style='width:100px;'/></a>
                </c:when>
            <c:when test="${noti.type == 'FollowStore' }">
                <a href='<c:url value="/store/profile/index.jsp.oo?storeId="/>${noti.store.id}'>${noti.store.name}</a>
                </c:when>
            <c:when test="${noti.type == 'LikeOffer' }">
                <a href='<c:url value="/offer/details/index.jsp.oo?offerId="/>${noti.offer.id}'><img src='${noti.offer.largeImg }' style='width:100px;'/></a>
                </c:when>
            <c:when test="${noti.type == 'FollowUser' }">
                <a href='<c:url value="/user/profile/index.jsp.oo?userId="/>${noti.param}'>user details</a>
           </c:when>
            <c:when test="${noti.type == 'ShareOffer' }">
                <a href='<c:url value="/offer/details/index.jsp.oo?offerId="/>${noti.offer.id}'><img src='${noti.offer.largeImg }' style='width:100px;'/></a>
            </c:when>
        </c:choose>
    </div>
</c:forEach>


            