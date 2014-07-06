<%@page import="java.util.Properties"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/onsalelist" prefix="a" %>

<%
	String url = "/ws/cj/link-details?format=object&" + request.getQueryString();
	request.getRequestDispatcher(url).include(request, response);
%>

<table>
<tr><td>advertiserId</td><td>${it.advertiserId}</td></tr>
<tr><td>clickCommission</td><td>${it.clickCommission}</td></tr>
<tr><td>creativeHeight</td><td>${it.creativeHeight}</td></tr>
<tr><td>creativeWidth</td><td>${it.creativeWidth}</td></tr>
<tr><td>leadCommission</td><td>${it.leadCommission}</td></tr>
<tr><td>linkCodeHtml</td><td>${it.linkCodeHtml}</td></tr>
<tr><td>linkCodeJavascript</td><td>${it.linkCodeJavascript}</td></tr>
<tr><td>linkDestination</td><td>${it.linkDestination}</td></tr>
<tr><td>linkId</td><td>${it.linkId}</td></tr>
<tr><td>linkName</td><td>${it.linkName}</td></tr>
<tr><td>linkType</td><td>${it.linkType}</td></tr>
<tr><td>advertiserName</td><td>${it.advertiserName}</td></tr>
<tr><td>promotionType</td><td>${it.promotionType}</td></tr>
<tr><td>promotionStartDate</td><td>${it.promotionStartDate}</td></tr>
<tr><td>promotionEndDate</td><td>${it.promotionEndDate}</td></tr>
<tr><td>relationshipStatus</td><td>${it.relationshipStatus}</td></tr>
<tr><td>salecommission</td><td>${it.salecommission}</td></tr>
<tr><td>sevenDayEpc</td><td>${it.sevenDayEpc}</td></tr>
<tr><td>threeMonthEpc</td><td>${it.threeMonthEpc}</td></tr>
<tr><td>categoryId</td><td>${it.categoryId}</td></tr>
</table>

