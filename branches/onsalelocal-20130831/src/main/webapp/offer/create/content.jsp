<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<h3>Create Offer</h3>

<form id="offerForm" method="post" action='<c:url value="/ws/v2/offer"/>'>
<input type="hidden" name="start"/>
<input type="hidden" name="end"/>
<table>
    <o:text-input-tr id="title"></o:text-input-tr>
    <o:text-input-tr id="price"></o:text-input-tr>
    <o:text-input-tr id="discount"></o:text-input-tr>
    <o:text-input-tr id="merchant"></o:text-input-tr>
    <o:text-input-tr id="address"></o:text-input-tr>
    <o:text-input-tr id="city"></o:text-input-tr>
    <o:text-input-tr id="state"></o:text-input-tr>
    <o:text-input-tr id="country"></o:text-input-tr>
    <o:text-input-tr id="phone"></o:text-input-tr>
    <o:text-input-tr label="Start Date" id="startDate"></o:text-input-tr>
    <o:text-input-tr label="End Date" id="endDate"></o:text-input-tr>
    <o:text-input-tr id="tags"></o:text-input-tr>
    <o:text-input-tr id="category"></o:text-input-tr>
    <o:text-input-tr id="subcategory"></o:text-input-tr>
    <o:text-input-tr id="largeImg"></o:text-input-tr>
    <o:tr-label-value label="File"><input type="file" name="image" id="image"/></o:tr-label-value>
    <o:textarea-tr id="description"></o:textarea-tr>
    <o:tr-label-value><input type="button" value="submit" onclick="createOffer()"/></o:tr-label-value>
</table>
</form>

<script type="text/javascript">
$(document).ready(function(){
    var tomorrow = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
    $('#startDate').val(tomorrow.format('yyyy/MM/dd'));
    var day7 = new Date(new Date().getTime() + 7 * 24 * 60 * 60 * 1000);
    $('#endDate').val(day7.format('yyyy/MM/dd'));
});

function createOffer() {
    $('#start').val(Date.parse($('#startDate')));
    $('#end').val(Date.parse($('#endDate')));
    
    $("#offerForm").ajaxForm({
        success:function(data) { 
            var url = serverBase + "/offer/details/index.jsp.oo?offerId=" + data.id;
            window.location = url;
         },
         dataType:"json"
       }).submit();    
    
	return false;
}
</script>
