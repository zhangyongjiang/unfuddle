<%@page import="com.gaoshin.beans.User"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<div id="msgContent">
<% 	
	String userId = request.getParameter("to");
	String pageUrl = "/user/profile-by-id?format=object&var=user&id=" + userId;
%>
<jsp:include page="<%=pageUrl%>"></jsp:include>

<div style="float:right;margin-top:-10px;">
<img src='<g:user-icon user="${user}"/>' 
	onclick='window.location="<c:url value="/m/dating/profile/index.jsp.oo?uid=${user.id}"/>"'
	style="border:solid 1px gold;width:80px;margin:0 0 0 6px;" />
</div>

<h3><span style="color:#666;font-weight:normal;font-size:0.8em;">Chat with</span> ${user.name } <span style="color:#666;font-weight:normal;font-size:0.8em;">${user.age}</span></h3> 
<div style="margin:-15px 0 0 10px;font-weight:normal;"><span style="color:#666;">Interests:</span><b> ${fn:replace(user.interests, newLineChar, ", ")}</div>

<c:if test="${not empty user.currentLocation }">
<div style="margin:0 0 0 10px;font-weight:normal;"><span style="color:#666;">Location:</span> ${user.currentLocation.city}, ${user.currentLocation.state} </div>
</c:if>

<div style='clear:both;height:1px;'>&nbsp;</div>

<div style="margin-top:4px;border:solid 1px #bbb;width:100%;overflow-y:scroll;padding-top:4px;" id="messageBox">
</div>

<div id="typing" style="margin-top:6px;position:absolute;width:100%;left:3px;">
<input style="width:98%;height:3em;" id="inputArea" type="text" data-theme='c'>
<button id="btnSend" type="button" data-theme='b'>Send</button>
</div>

</div>
<script type="text/javascript" >
function formatDate(date) {
    var month = date.getMonth()+1;
    var day = date.getDate();
    var hour = date.getHours();
    if(hour<10)
        hour = "0" + hour;
    var minute = date.getMinutes();
    if(minute<10)
        minute = "0" + minute;
    var sec = date.getSeconds();
    if(sec<10)
        sec = "0" + sec;
    
    return month + "/" + day + " " + hour + ":" + minute + ":" + sec;
}

$(document).ready(function(){
    var btnWidth = $('#btnSend').width();
    var width = $('#viewarea').width();
    var height = $('#viewarea').height();
    var inputAreaWidth = width-btnWidth-15;
    $('#typing').css({top:height-100});
    
    var top = $('#msgContent').position().top;
    $('#msgContent').css({height:height-top});
    
    var msgBoxHeight = $('#typing').position().top - $('#messageBox').position().top - 30;
    $('#messageBox').css({height:msgBoxHeight});
    
    $('#inputArea').change(function(){sendMsg();});
    $(document).everyTime(3000, function(){checkMsg();});
    $('#btnSend').click(function(){sendMsg();});
    checkMsg();
});

var lastSeenId = 0;
var checking = false;
var lastSentTime = new Date().getTime();
function checkMsg() {
    // check if idling too long
    var now = new Date().getTime();
    if((now - lastSentTime) > 120000) {
        window.location = '<c:url value="/m/poem/random/index.jsp.oo"/>';
        return false;
    }
    
    if(checking)
        return true;
    
    Device.resetLastAccessTime();
    checking = true;
    try {
        var base = '<c:url value="/message/list"/>';
	    var url = base + "?format=json&who=${user.id}&since=" + lastSeenId + "&random=" + new Date().getTime();
		$.ajax({
			url : url,
			type : "GET",
			headers: {
				"Content-Type" : "application/json; charset=utf-8",
				"Accept": "application/json; charset=utf-8"
			},
			complete : function(transport) {
				if (transport.status == 200) {
				    var msgs = JSON.parse(transport.responseText);
				    if(msgs == null)
				        return;
				    for(var i=msgs.list.length-1; i>=0; i--) {
				        var msg = msgs.list[i];
				        var pos = "left";
				        var who = "ME";
				        if(msg.sender == '${user.id}') {
				            pos = 'right';
				            who = '${user.name}';
				        }
				        var html = "<div style='border-bottom:solid 1px #fff;clear:both;width:100%;height:1px;margin-bottom:3px;'>&nbsp;</div>"
				        		+ "<div style='padding:3px;text-align:" + pos + ";float:" + pos + ";'>" 
				        		+ "<span style='font-size:0.8em;color:#999;'>" + who + " " + formatDate(new Date(Number(msg.time))) + "</span>" 
				        		+ "<br/>" + msg.content + "</div>";
					    $('#messageBox').append(html);
				        lastSeenId = msg.id;
				    }
				    $("#messageBox").animate({ scrollTop: $("#messageBox").attr("scrollHeight") }, 3000);
				} else if (transport.status == 496){
				    window.location = '<c:url value="/m/user/login/index.jsp.oo"/>';
				} else {
				    var html = transport.responseText;
				    $('#messageBox').html($('#messageBox').html() + "<br/>" + html);
				    $("#messageBox").animate({ scrollTop: $("#messageBox").attr("scrollHeight") }, 3000);
				}
			}
		});
    } catch (e){
	    $('#messageBox').append(e);
	    $("#messageBox").animate({ scrollTop: $("#messageBox").attr("scrollHeight") }, 3000);
    }
    checking = false;
    return true;
}

function sendMsg() {
    lastSentTime = new Date().getTime();
	Device.showLoadingDialog();
    var msg = $('#inputArea').val();
    if(msg == null || msg.trim().length == 0)
        return;
    var type = 'Web';
    var subtype = null;
    var title = 'Message from ${me.name}';
    var url = '/m/message/send/index.jsp.oo?to=${me.id}';

    try {
	    //Device.sendSms('${user.encryptedId}', type, subtype, title, msg, url);
    } catch (e) {alert (e);}
    
    $('#inputArea').val("");
    
	var req = {
		receipt: '${user.encryptedId}',
		content: msg,
		time: new Date().getTime()
	};
	var json = JSON.stringify(req);
	
	$.ajax({
		  url:'<c:url value="/message/send"/>',
		  type:"POST",
		  data:json,
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  complete: function(transport) {
			     if(transport.status == 200) {
			     } else {
			         Device.toast('Error: ' + transport.responseText);
			     }
				Device.hideLoadingDialog();
			  }
	});

}

</script>
