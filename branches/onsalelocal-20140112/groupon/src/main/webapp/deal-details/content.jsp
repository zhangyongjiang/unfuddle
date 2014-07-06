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
	String url = "/ws/groupon/deal-details?format=object&" + (param == null ? "" : param);
	request.getRequestDispatcher(url).include(request, response);
%>

<div style='float:right;'><img src='${it.deal.largeImageUrl}'/></div>
<h2>${it.deal.title }</h2>
<div>${it.deal.merchant.name}</div>
<div>${it.deal.options.option[0].redemptionLocations.redemptionLocation[0].streetAddress1}</div>
<div>${it.deal.options.option[0].redemptionLocations.redemptionLocation[0].city}, 
${it.deal.options.option[0].redemptionLocations.redemptionLocation[0].state}</div>

<p>
${it.deal.pitchHtml }
</p>

<div style="font-size: 0.8em;color: #999;">
${it.deal.finePrint }
</div>