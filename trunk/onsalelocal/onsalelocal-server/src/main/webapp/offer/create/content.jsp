<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<%
    String companyListUrl = "/ws/company/list?size=500&var=companyList&format=object";
    request.getRequestDispatcher(companyListUrl).include(request, response);
%>

<div style="float:left;">
<h3>Create Offer</h3>

<form id="offerForm" method="post" action='<c:url value="/ws/v2/offer"/>'>
<input type="hidden" name="start"/>
<input type="hidden" name="end"/>
<table>
    <o:tr-label-value label="Merchant">
        <select id="merchantId" name="merchantId">
        <c:forEach var="company" items="${companyList.items }">
            <option value="${company.id }">${company.name}</option>
        </c:forEach>
        </select>
    </o:tr-label-value>
    
    <o:text-input-tr id="title"></o:text-input-tr>
    <o:text-input-tr id="price"></o:text-input-tr>
    
    <o:text-input-tr id="url"></o:text-input-tr>
    <o:text-input-tr label="Start Date" id="startDate"></o:text-input-tr>
    <o:text-input-tr label="End Date" id="endDate"></o:text-input-tr>
    
    <o:tr-label-value label="Category">
        <select id="category" name="category">
                <option value="Food & Grocery"      >Food & Grocery      </option>
                <option value="Home & Garden"       >Home & Garden    </option>
                <option value="Beauty & spa"        >Beauty & spa     </option>
                <option value="Events & Activities" >Events & Activities </option>
                <option value="Baby & Toddler"      >Baby & Toddler   </option>
                <option value="Sports & fitness"    >Sports & fitness </option>
                <option value="Shoes & Footwear"    >Shoes & Footwear </option>
                <option value="Clothing & Apparel"  >Clothing & Apparel  </option>
                <option value="Toys & Games"        >Toys & Games     </option>
                <option value="Electronics & computer">Electronics & computer</option>
                <option value="Travel & Luggage"    >Travel & Luggage </option>
                <option value="Dining & nightlife"  >Dining & nightlife  </option>
                <option value="Furniture"           >Furniture        </option>
                <option value="Health & wellness"   >Health & wellness</option>
                <option value="Automotive & car"    >Automotive & car </option>
                <option value="For Man"             >For Man          </option>
                <option value="For Woman"           >For Woman        </option>
                <option value="For Kids"            >For Kids         </option>
                <option value="Pets"                >Pets             </option>
                <option value="Others"              >Others           </option>
        </select>
    </o:tr-label-value>
    
    <o:text-input-tr id="tags"></o:text-input-tr>
    <o:text-input-tr id="largeImg"></o:text-input-tr>
    <o:tr-label-value label="File"><input type="file" name="image" id="image"/></o:tr-label-value>
    <o:textarea-tr id="description"></o:textarea-tr>
    <o:tr-label-value><input type="button" value="submit" onclick="createOffer()"/></o:tr-label-value>
</table>
</form>
</div>

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
