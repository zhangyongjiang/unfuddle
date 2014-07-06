<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/coupon" prefix="p" %>


<%
	String param = request.getQueryString();
	String url = "/ws/coupon/details?format=object&" + (param == null ? "" : param);
	request.getRequestDispatcher(url).include(request, response);
%>

<p:coupon-details coupon="${it }"></p:coupon-details>

<script type="text/javascript">
$(document).ready(function(){
});
</script>
