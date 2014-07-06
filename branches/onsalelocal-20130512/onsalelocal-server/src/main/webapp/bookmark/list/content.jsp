<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<%
	String url = "/ws/bookmark/list-details?format=object";
	request.getRequestDispatcher(url).include(request, response);
%>

<c:if test="${empty it}">
	No bookmark found.
</c:if>

<c:if test="${not empty it}">
        ${fn:length(it)} bookmark(s) found.
    <table style="border:solid 1px;width:96%;">
        <c:forEach var="bk" items="${it}">
        <tr>
            <td style="vertical-align:top;width:12px;padding:3px;border-top:solid 1px #ccc;"><img width="100" src='${bk.offer.smallImg}'/></td>
            <td style="padding:3px;border-top:solid 1px #ccc;vertical-align:top;">
		<div><a href='${bk.offer.url }'>${bk.offer.title}</a></div>
			<div style="font-size:1.6em;">$${bk.offer.price} <c:if test="${not empty bk.offer.discount}"><span style='color:red;'>${bk.offer.discount} off</span></c:if></div>
            			<div style="margin-top:10px;">${bk.offer.merchant}</div>
				<div style="font-size:0.8em;color:#666;margin-top:10px;">${bk.offer.address}</div>
				<div style="font-size:0.8em;color:#666;">${bk.offer.city}, ${bk.offer.state}</div>
				<c:if test="${not empty bk.offer.rootSource}"><div style="margin-top:10px;font-size:0.8em;color:#bbb;">${bk.offer.rootSource}</div></c:if>
				<c:if test="${empty bk.offer.rootSource}"><div style="margin-top:10px;font-size:0.8em;color:#bbb;">${bk.offer.source}</div></c:if>
				
				<div style="float:right;margin-top:-24px;">
					<a href="javascript:void(0);" onclick='unbookmark("${bk.offer.id}")'>unbookmark</a>
				</div>
            </td></tr>
        </c:forEach>
    </table>
</c:if>

<script type="text/javascript">
$(document).ready(function(){
});

function unbookmark(offerid) {
	var url = serverBase + "/ws/bookmark/remove/" + offerid;
	$.ajax({
		  url:url,
		  type:"POST",
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  complete: function(transport) {
			     if(transport.status == 200) {
			         window.location.reload();
			     } else {
			         alert('Error: ' + transport.status + ", " + transport.responseText);
			     }
			  }
		});

	return false;
}
</script>
