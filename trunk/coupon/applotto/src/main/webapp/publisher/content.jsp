<%@page import="java.net.URLEncoder"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%
	String url = "/ws/user/details?format=object&" + request.getQueryString();
	request.getRequestDispatcher(url).include(request, response);
%>

<img style="margin-top:16px;float:right;margin-left:24px;" width="160" src='${it.icon }'/>
<h1>${it.name }</h1>

<div style=""><p>
${it.description }</p></div>

<div style="clear:both;height:1px;"></div>

<div style="margin-top:16px;">
<c:if test="${empty it.applicationDetailsList.items}">
	User currently has no application for you to earn rewards.
</c:if>

<c:if test="${not empty it.applicationDetailsList.items}">
    <div>Applications from ${it.name }</div>
	<c:forEach var="app" items="${it.applicationDetailsList.items}">
		<div style="margin:10px;border:solid 1px #bbb;padding:10px;clear:both;width:96%;">
            <div style="margin-right:20px;">
                <p><img width="96" style="float:left;margin-right:16px;" src='${app.icon }'/>
                <strong>${app.name }</strong>
                ${app.description }
                </p>
            </div>
            <div style="clear:both;height:1px;width:100%;">&nbsp;</div>
            <div style="clear:both;width:100%;">
                <c:if test="${empty app.activityRewardList.items }">
                    <div style=""><img width="48" src='<c:url value="/images/gift.png"/>'/></div>
                    <div>No reward for this application.</div> 
                </c:if>
                <c:forEach var="reward" items="${app.activityRewardList.items }">
                    <div style=""><img width="48" src='<c:url value="/images/gift.png"/>'/></div>
                    <div style="margin-left:72px;margin-top:-48px;">
                        <c:if test="${app.userId == me.id }"><a href='<c:url value="/user/edit-reward/index.jsp.oo?rewardId="/>${reward.id}'>
                        <strong>Rewards: ${reward.points} points for ${reward.type }</strong>
                        </a>
                        </c:if>
                        <c:if test="${app.userId != me.id }">
                        <strong>Rewards: ${reward.points} points for ${reward.type }</strong>
                        </c:if>
                        ${reward.terms }
                        <c:if test="${reward.type == 'Install' }">
                            <button onclick='window.location="https://play.google.com/store/apps/details?id=${app.marketId }"'>install now</button>
                        </c:if>
                    </div>
                    <div style="clear:both;height:1px;width:100%;margin-bottom:32px;">&nbsp;</div>
                </c:forEach>
            </div>
            <div style="clear:both;width:100%;">
                <a href='<c:url value="/user/add-reward/index.jsp.oo?applicationId=${app.id}"/>'>Add More Rewards</a>
            </div>
		</div>
        <div style="clear:both;height:1px;width:100%;">&nbsp;</div>
	</c:forEach>
</c:if>

<o:user>
    <div style="">
    <button onClick='window.location=serverBase + "/user/add-application/index.jsp.oo"'>Add Application</button>
    </div>
</o:user>

</div>

<script type='text/javascript'>
function addReward(appid) {
    var req = {
            name: $('#name').val(),
            type: $('#type').val(),
            marketId: $('#marketId').val(),
            icon: $('#icon').val(),
            image: $('#image').val(),
            description: $('#description').val()
    };
    var json = JSON.stringify(req);
    var url = serverBase + "/ws/user/application/reward/add";
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