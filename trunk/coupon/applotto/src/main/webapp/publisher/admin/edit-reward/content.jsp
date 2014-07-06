<%@page import="com.gaoshin.appbooster.bean.CampaignDetails"%>
<%@page import="com.gaoshin.appbooster.bean.RewardDetails"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<%
String campaignId = request.getParameter("campaignId");
String rewardId = request.getParameter("rewardId");
if(rewardId != null) {
    request.getRequestDispatcher("/ws/user/reward-details?format=object&var=reward&rewardId=" + rewardId).include(request, response);
    RewardDetails reward = (RewardDetails)request.getAttribute("reward");
    campaignId = reward.getCampaignId();
}
else if(campaignId != null) {
    RewardDetails reward = new RewardDetails();
    request.getRequestDispatcher("/ws/user/campaign-details?format=object&var=campaign&campaignId=" + campaignId).include(request, response);
    CampaignDetails campaign = (CampaignDetails) request.getAttribute("campaign");
    reward.setCampaignDetails(campaign);
    request.setAttribute("reward", reward);
}
else {
    out.write("Wrong request");
    return;
}
%>

<div style="width:100%;">
<h3>
<c:if test="${empty reward.id }">Add Reward</c:if>
<c:if test="${not empty reward.id }">Edit Reward</c:if>
</h3>
</div>

<form onsubmit="return insertOrUpdate();">
<input type="hidden" name="campaignId" id="campaignId" value='<o:context name="campaignId"/>'/>
<input type="hidden" name="rewardId" id="rewardId" value='<o:context name="rewardId"/>'/>
<table style="width: 100%;">
<o:tr-label-value label="For Campaign" value="${campaign.name}"/>
<o:text-input-tr label="Name" id="name" value="${reward.name }"/>
<o:text-input-tr label="Type" id="type" value="${reward.type }"/>
<o:text-input-tr label="Custom Type" id="subtype" value="${reward.subtype }"/>
<o:text-input-tr label="Points" id="points"/>
<o:tr label="Description">
    <textarea id="description" name="description" style="width:80%;height:240px;">${reward.description}</textarea>
</o:tr>
<c:if test="${empty reward.id }"><o:submit-tr value="Add"/></c:if>
<c:if test="${not empty reward.id }"><o:submit-tr value="Update"/></c:if>
</table>
</form>

<script type="text/javascript">

function insertOrUpdate() {
	<c:if test="${empty reward.id }">insert();</c:if>
	<c:if test="${not empty reward.id }">update();</c:if>
}

function insert() {
	var req = {
			campaignId: $('#campaignId').val(),
			name: $('#name').val(),
            type: $('#type').val(),
            subtype: $('#subtype').val(),
            points: $('#points').val(),
			description: $('#description').val()
	};
	var json = JSON.stringify(req);
	var url = serverBase + "/ws/user/add-reward";
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
            id: $('#rewardId').val(),
			campaignId: $('#campaignId').val(),
			name: $('#name').val(),
            type: $('#type').val(),
            subtype: $('#subtype').val(),
            points: $('#points').val(),
			description: $('#description').val()
    };
    var json = JSON.stringify(req);
    var url = serverBase + "/ws/user/update-reward";
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
