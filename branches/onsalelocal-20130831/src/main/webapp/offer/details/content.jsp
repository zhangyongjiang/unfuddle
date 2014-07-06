<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<%
    String param = request.getQueryString();
    String url = "/ws/v2/offer/details?format=object&" + (param == null ? "" : param);;
    request.getRequestDispatcher(url).include(request, response);
%>

<h3>Offer Detail</h3>

${it.title} <div style="font-size:0.8em;color:#aaa;">Created by ${it.submitter.firstName } at <o:millisecond-to-date time="${it.created }"/></div>
<table style="border:solid 1px #bbb;">
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
    <o:tr-label-value id="largeImg"><img style="width:480px;" src='${it.largeImg}'/></o:tr-label-value>
    <o:tr-label-value id="description" value="${it.description}"></o:tr-label-value>
</table>

<a target="_self" href='<c:url value="/offer/edit/index.jsp.oo?offerId="/>${it.id}'>edit</a>
