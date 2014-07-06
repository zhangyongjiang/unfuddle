<%@page import="java.net.URLEncoder"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%
	String url = "/ws/store/tops?format=object";
	request.getRequestDispatcher(url).include(request, response);
%>

<c:if test="${empty it.items}">
	You have no store currently.
</c:if>

<c:if test="${not empty it.items}">
	<c:forEach var="store" items="${it.items}">
		<div style="margin:10px;border:solid 1px #bbb;padding:10px;">
			<h3>${store.name}</h3>
			<img src="${store.logo}"/>
			<p>
			name: ${store.name}<br/>
			address: ${store.address}<br/>
			address2: ${store.address2}<br/>
			city: ${store.city}<br/>
			state: ${store.state}<br/>
			country: ${store.country" value="US}<br/>
			zipcode: ${store.zipcode}<br/>
			phone: ${store.phone}<br/>
			email: ${store.email}<br/>
			web: ${store.web}<br/>
			categoryId: ${store.categoryId}<br/>
			logo: ${store.logo}<br/>
			latitude: ${store.latitude}<br/>
			longitude: ${store.longitude}<br/>
			</p>
		</div>
	</c:forEach>
</c:if>

<script type="text/javascript">
	$(document).ready(function(){
		log('<%=url%>');
		log('<%=escaped%>');log('<o:sourcecode-loc obj="${it}"/>');

		log('<div style="clear:both;height:10px;margin-bottom:12px;">&nbsp;</div>');
	});
</script>
