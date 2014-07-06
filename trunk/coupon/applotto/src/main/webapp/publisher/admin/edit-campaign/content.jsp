<%@page import="com.gaoshin.appbooster.bean.ApplicationDetails"%>
<%@page import="com.gaoshin.appbooster.bean.CampaignDetails"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<%
String applicationId = request.getParameter("applicationId");
String campaignId = request.getParameter("campaignId");
if(campaignId != null) {
    request.getRequestDispatcher("/ws/user/campaign-details?format=object&var=campaign&campaignId=" + campaignId).include(request, response);
    CampaignDetails campaign = (CampaignDetails)request.getAttribute("campaign");
    applicationId = campaign.getApplicationId();
}
else if(applicationId != null) {
    CampaignDetails campaign = new CampaignDetails();
    request.getRequestDispatcher("/ws/user/application-details?format=object&var=app&applicationId=" + applicationId).include(request, response);
    ApplicationDetails app = (ApplicationDetails) request.getAttribute("app");
    campaign.setApplicationDetails(app);
    request.setAttribute("campaign", campaign);
}
else {
    out.write("Wrong request");
    return;
}
%>

<div style="width:100%;">
<h3>
<c:if test="${empty campaign.id }">Add Campaign</c:if>
<c:if test="${not empty campaign.id }">Edit Campaign</c:if>
</h3>
</div>

<form onsubmit="return insertOrUpdate();">
<input type="hidden" name="applicationId" id="applicationId" value='<o:context name="applicationId"/>'/>
<input type="hidden" name="campaignId" id="campaignId" value='<o:context name="campaignId"/>'/>
<table style="width: 100%;">
<o:tr label="Application"><div style="float:left;"><img width="48" src='${application.icon}'/></div><div style="float:left;margin-left:16px;margin-top:16px;"><strong>${application.name}</strong></div></o:tr>
<o:text-input-tr label="Name" id="name"/>
<o:text-input-tr label="Start Date" id="startDate"/>
<o:text-input-tr label="End Date" id="endDate"/>
<o:text-input-tr label="Budget" id="budget" value="${campaign.budget }"/>
<o:tr label="Terms">
    <textarea id="terms" name="terms" style="width:80%;height:240px;">${campaign.terms}</textarea>
</o:tr>
<c:if test="${empty campaign.id }"><o:submit-tr value="Add"/></c:if>
<c:if test="${not empty campaign.id }"><o:submit-tr value="Update"/></c:if>
</table>
</form>

<script type="text/javascript">
<c:if test="${not empty campaign.id }">
    $(document).ready(function(){
        var date = new Date(${campaign.start});
        $('#startDate').val(date.format('mm/dd/yyyy'));
        
        var date = new Date(${campaign.end});
        $('#endDate').val(date.format('mm/dd/yyyy'));
    });
</c:if>

function insertOrUpdate() {
	<c:if test="${empty campaign.id }">insert();</c:if>
	<c:if test="${not empty campaign.id }">update();</c:if>
}

function insert() {
	var req = {
			applicationId: $('#applicationId').val(),
			name: $('#name').val(),
            budget: $('#budget').val(),
			start: Date.parse($('#startDate').val()),
            end: Date.parse($('#endDate').val()),
			terms: $('#terms').val()
	};
	var json = JSON.stringify(req);
	var url = serverBase + "/ws/user/add-campaign";
	$.ajax({
		  url: url,
		  type:"POST",
		  data:json,
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  complete: function(transport) {
			     if(transport.status == 200) {
                     var app = JSON.parse(transport.responseText);
			         self.location = serverBase + '/user/index.jsp.oo';
			     } else {
			         alert('Error: ' + transport.responseText);
			     }
			  }
		});

	return false;
}

function update() {
    var req = {
            name: $('#name').val(),
            id: $('#campaignId').val(),
            budget: $('#budget').val(),
            start: Date.parse($('#startDate').val()),
            end: Date.parse($('#endDate').val()),
            terms: $('#terms').val()
    };
    var json = JSON.stringify(req);
    var url = serverBase + "/ws/user/update-campaign";
    $.ajax({
          url: url,
          type:"POST",
          data:json,
          contentType:"application/json; charset=utf-8",
          dataType:"json",
          complete: function(transport) {
                 if(transport.status == 200) {
                     var app = JSON.parse(transport.responseText);
                     self.location = serverBase + '/user/index.jsp.oo';
                 } else {
                     alert('Error: ' + transport.responseText);
                 }
              }
        });

    return false;
}
</script>
