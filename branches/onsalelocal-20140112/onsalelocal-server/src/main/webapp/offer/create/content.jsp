<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<div style="float:left;">
<h3>Create Offer</h3>

<form id="offerForm" method="post" action='<c:url value="/ws/v2/offer"/>'>
<input type="hidden" name="start"/>
<input type="hidden" name="end"/>
<input type="hidden" name="merchantId"/>
<table>
    <o:text-input-tr id="title"></o:text-input-tr>
    <o:text-input-tr id="price"></o:text-input-tr>
    <o:text-input-tr id="merchant"></o:text-input-tr>
    <o:text-input-tr id="phone"></o:text-input-tr>
    <o:text-input-tr id="url"></o:text-input-tr>
    <o:text-input-tr label="Start Date" id="startDate"></o:text-input-tr>
    <o:text-input-tr label="End Date" id="endDate"></o:text-input-tr>
    <o:text-input-tr id="tags"></o:text-input-tr>
    <o:text-input-tr id="largeImg"></o:text-input-tr>
    <o:tr-label-value label="File"><input type="file" name="image" id="image"/></o:tr-label-value>
    <o:textarea-tr id="description"></o:textarea-tr>
    <o:tr-label-value label="apply to same name stores">
		<input type="radio" name="applyToAll" value="true"> true&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="radio" name="applyToAll" value="false" checked> false 
    </o:tr-label-value>
    <o:tr-label-value><input type="button" value="submit" onclick="createOffer()"/></o:tr-label-value>
</table>
</form>
</div>

<div style="float:right;" >
    <h3>Search Result</h3>
    <div id="searchResult"></div>
</div>

<script type="text/javascript">
function searchStore() {
    var merchant = $('#merchant').val();
    var city = $('#city').val();
    var phone = $('#phone').val();
    var url = serverBase + "/ws/store/search?merchant=" + escape(merchant) + "&city=" + escape(city) + "&phone=" + escape(phone);
    $.ajax({
        url:url,
        type:"GET",
        contentType:"application/json; charset=utf-8",
        dataType:"json",
        complete: function(transport) {
               if(transport.status == 200) {
            	   var resp = JSON.parse(transport.responseText);
            	   displaySearchResult(resp);
               } else {
                   alert('Error: ' + transport.status + ", " + transport.responseText);
               }
            }
      });
}

var searchResult;
function displaySearchResult(resp) {
	searchResult = resp;
	if(resp.response.docs.length == 0) {
		$('#searchResult').html("no search result found");
		return;
	}
	var html = "";
	for(var i=0; i<resp.response.docs.length; i++) {
		var doc = resp.response.docs[i];
		html = html + "<div style='border:solid 1px #bbb;margin:6px;padding:4px;'>" + getHtmlForSearch(i, doc) + "</div>";
	}
	$('#searchResult').html(html);
}

function selectDoc(index) {
    $('#merchant').val(searchResult.response.docs[index].name[0]);
    $('#merchantId').val(searchResult.response.docs[index].id);
    $('#phone').val(searchResult.response.docs[index].phone);
    $('#latitude').val(searchResult.response.docs[index].location_0_coordinate);
    $('#longitude').val(searchResult.response.docs[index].location_1_coordinate);
}

function getHtmlForSearch(index, doc) {
	var html = "<a href='javascript:void(0)' onclick='selectDoc(" + index + ")'>";
    html += doc.name[0] + "<br/>";
    html += doc.address + "<br/>";
    html += doc.city + "<br/>";
    html += doc.state + "<br/>";
    html += doc.phone + "<br/>";
	html += "</a>";
	return html;
}

$(document).ready(function(){
    var tomorrow = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
    $('#startDate').val(tomorrow.format('yyyy/MM/dd'));
    var day7 = new Date(new Date().getTime() + 7 * 24 * 60 * 60 * 1000);
    $('#endDate').val(day7.format('yyyy/MM/dd'));
    
    $('#merchant').change(searchStore);
    $('#city').change(searchStore);
    $('#phone').change(searchStore);
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

function addTag() {
	var tag = $('#defcats').val();
	var old = $('#tags').val();
	if(old.indexOf(tag)==-1) {
		if(old.length == 0)
			$('#tags').val(tag);
		else
            $('#tags').val(old+";"+tag);
	}
}
</script>
