<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<%
	String param = request.getQueryString();
	String url = "/ws/offer/details?format=object&var=deal&" + (param == null ? "" : param);
	request.getRequestDispatcher(url).include(request, response);
%>

<h3><a href='${deal.url }'>${deal.title }</a></h3>

<table><tr>
     <td style="vertical-align:top;width:12px;padding:3px;border-top:solid 1px #ccc;">
     	<c:if test="${not empty deal.largeImg }"><a href='${deal.url }'><img src='${deal.largeImg}'/></a></c:if>
     	<c:if test="${empty deal.largeImg }"><a href='${deal.url }'><img src='${deal.smallImg}'/></a></c:if>
     </td>
     <td style="padding:3px;border-top:solid 1px #ccc;vertical-align:top;">
                 <div style="font-size:1.6em;">$${deal.price} <c:if test="${not empty deal.discount}"><span style='color:red;'>${deal.discount} off</span></c:if></div>
                         <div style="margin-top:10px;">${deal.merchant}</div>
                         <div style="font-size:0.8em;color:#666;margin-top:10px;">${deal.phone}</div>
                         <div style="font-size:0.8em;color:#666;margin-top:10px;">${deal.address}</div>
                         <div style="font-size:0.8em;color:#666;">${deal.city}, ${deal.state}</div>

                         <div style="float:left;margin-top:10px;font-size:0.8em;color:#bbb;"><o:millisecond-to-date time="${deal.updated}"/></div>
                         <c:if test="${not empty deal.rootSource}"><div style="margin-left:16px;float:left;margin-top:10px;font-size:0.8em;color:#bbb;"><a href='${deal.url }'>${deal.rootSource}</a></div></c:if>
                         <c:if test="${empty deal.rootSource}"><div style="margin-left:16px;float:left;margin-top:10px;font-size:0.8em;color:#bbb;"><a href='${deal.url }'>${deal.source}</a></div></c:if>
                         <div style="float:left;margin-top:10px;font-size:0.8em;color:#bbb;margin-left:16px;">${deal.category} ${deal.subcategory} ${deal.tags}</div>
                         <c:if test="${not empty me.id}">
                                 <div style="float:right;margin-top:-24px;">
                                 <c:if test="${deal.bookmarked}"><a href="javascript:void(0);" onclick='unbookmark("${deal.id}")'>unbookmark</a></c:if>
                                 <c:if test="${!deal.bookmarked}"><a href="javascript:void(0);" onclick='bookmark("${deal.id}")'>bookmark</a></c:if>
                                 </div>
                         </c:if>
     </td>
  </tr>
</table>