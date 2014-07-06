<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>

<%
    String param = request.getQueryString();
    String url = "/ws/v2/offer/details?format=object&" + (param == null ? "" : param);;
    request.getRequestDispatcher(url).include(request, response);
    
    String url2 = "/ws/v2/offer/comments?var=comments&format=object&" + (param == null ? "" : param);;
    request.getRequestDispatcher(url2).include(request, response);
%>

<osl:ws path="<%=url + " " + url2%>"/>
<h1>${it.title}</h1>

<c:if test="${not empty it.submitter }">
<div style="font-size:0.8em;color:#aaa;">Created by <a target='_self' href='<c:url value="/user/profile/index.jsp.oo?userId="/>${it.submitter.id}'>${it.submitter.firstName }</a> at <o:millisecond-to-date time="${it.created }"/></div>
</c:if>

<div style="float:left;margin:12px;">
<img style="height:480px;" src='${it.largeImg}'/>
</div>

<div style="float:left;margin:12px;">
    <table style="border:solid 0px #bbb;">
        <o:tr-label-value id="price" value="${it.price}"></o:tr-label-value>
        <o:tr-label-value id="discount" value="${it.discount}"></o:tr-label-value>
        <o:tr-label-value id="merchant" value="${it.merchant}"></o:tr-label-value>
        <o:tr-label-value id="address" value="${it.address}"></o:tr-label-value>
        <o:tr-label-value id="city" value="${it.city}"></o:tr-label-value>
        <o:tr-label-value id="state" value="${it.state}"></o:tr-label-value>
        <o:tr-label-value id="country" value="${it.country}"></o:tr-label-value>
        <o:tr-label-value id="phone" value="${it.phone}"></o:tr-label-value>
        <o:tr-label-value id="start" > <o:millisecond-to-date format="yyyy/MM/dd" time="${it.start}"></o:millisecond-to-date> </o:tr-label-value>
        <o:tr-label-value id="end" > <o:millisecond-to-date format="yyyy/MM/dd" time="${it.end}"></o:millisecond-to-date> </o:tr-label-value>
        <o:tr-label-value id="latitude" value="${it.latitude}"></o:tr-label-value>
        <o:tr-label-value id="longitude" value="${it.longitude}"></o:tr-label-value>
        <o:tr-label-value id="tags" value="${it.tags}"></o:tr-label-value>
        <o:tr-label-value id="description" value="${it.description}"></o:tr-label-value>
    </table>
</div>

<div style="clear:both;height:1px;"></div>
<div style="float:left;margin:6px;">
    <a target="_self" href='<c:url value="/offer/edit/index.jsp.oo?offerId="/>${it.id}'>edit</a>
</div>
<div style="float:left;margin:6px;">
    <a href='javascript:void(0);' onclick="comment()">comment</a>
</div>
<div style="float:left;margin:6px;">
    <osl:like cnt="${it.likes }" liked="${it.liked }" id="${it.id }"></osl:like>
</div>

<div style="clear:both;height:1px;"></div>
<h3>Comments (${fn:length(comments.items) })</h3>
<c:forEach var="comment" items="${comments.items }">
    <div style="margin:10px;">
    ${comment.content }
    <div style="color:#bbb;font-size:0.8em;">${comment.user.firstName } ${comment.user.lastName } <o:millisecond-to-date time="${comment.updated }"></o:millisecond-to-date></div>
    </div>
</c:forEach>

<script type="text/javascript">
function comment() {
	var comment = prompt("Please give your comment");
	if(!comment)
		return;
	var url = serverBase + "/ws/v2/offer/comment";
	var req = {
		"offerId": '${it.id}',
		"content": comment
	};
    $.ajax({
          url: url,
          type:"POST",
          contentType:"application/json; charset=utf-8",
          dataType:"json",
          data: JSON.stringify(req),
          complete: function(transport) {
              if(transport.status == 200) {
                  window.location.reload();
              } else {
                  alert('Error: ' + transport.responseText);
              }
           }
        });
}
</script>
