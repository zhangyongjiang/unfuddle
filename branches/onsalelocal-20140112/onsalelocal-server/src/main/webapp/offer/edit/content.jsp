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

<h3>Edit Offer</h3>

<form id="offerForm" onsubmit="return editOffer();" method="post" action='<c:url value="/ws/v2/offer"/>'>
    <input type="hidden" name="id" id="id" value="${it.id }"/>
    <input type="hidden" id="latitude" value="${it.latitude}"/>
    <input type="hidden" id="longitude" value="${it.longitude}"/>
	<input type="hidden" name="start"/>
	<input type="hidden" name="end"/>
<table>
    <o:text-input-tr id="title" value="${it.title}"></o:text-input-tr>
    <o:text-input-tr id="price" value="${it.price}"></o:text-input-tr>
    <o:text-input-tr id="discount" value="${it.discount}"></o:text-input-tr>
    <o:text-input-tr id="merchant" value="${it.merchant}"></o:text-input-tr>
    <o:text-input-tr id="address" value="${it.address}"></o:text-input-tr>
    <o:text-input-tr id="city" value="${it.city}"></o:text-input-tr>
    <o:text-input-tr id="state" value="${it.state}"></o:text-input-tr>
    <o:text-input-tr id="country" value="${it.country}"></o:text-input-tr>
    <o:text-input-tr id="phone" value="${it.phone}"></o:text-input-tr>
    <o:text-input-tr id="startDate" ><o:millisecond-to-date format="yyyy/MM/dd" time="${it.start}"></o:millisecond-to-date></o:text-input-tr>
    <o:text-input-tr id="endDate" ><o:millisecond-to-date format="yyyy/MM/dd" time="${it.end}"></o:millisecond-to-date></o:text-input-tr>
    <o:text-input-tr id="tags" value="${it.tags}"></o:text-input-tr>
    <o:text-input-tr id="largeImg" value="${it.largeImg}"></o:text-input-tr>
    <o:tr-label-value label="File"><input type="file" name="image" id="image"/></o:tr-label-value>
    <o:textarea-tr id="description" value="${it.description}"></o:textarea-tr>
    <o:submit-tr></o:submit-tr>
</table>
</form>

<script type="text/javascript">
var processing = false;
function editOffer() {
	if(processing) return false;
	processing = true;
    $('#start').val(Date.parse($('#startDate')));
    $('#end').val(Date.parse($('#endDate')));
    
    $("#offerForm").ajaxForm({
        success:function(data) { 
            var url = serverBase + "/offer/details/index.jsp.oo?offerId=" + data.id;
            window.location = url;
         },
         dataType:"json"
       }).submit();    
	processing = false;
	return false;
}
</script>
