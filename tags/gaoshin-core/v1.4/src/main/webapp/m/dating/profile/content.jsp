<%@page import="com.gaoshin.beans.User"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<% 	
	User me = (User) request.getAttribute("me");
	String userId = request.getParameter("uid");
	if(userId == null)
	    userId = me.getId()+"";
	String pageUrl = "/user/profile-by-id?format=object&var=user&id=" + userId;
%>
<jsp:include page="<%=pageUrl%>"></jsp:include>
<jsp:include page="/user/my-friends?format=object&var=myfriends"></jsp:include>	

<c:set var="isMyFriend">false</c:set>
<c:forEach var="friend" items="${myfriends.list}">
	<c:if test="${friend.id == user.id }">
		<c:set var="isMyFriend">true</c:set>
	</c:if>
</c:forEach>

<% User u = (User) request.getAttribute("user");
if(u.getCurrentLocation()!=null) {
String url = "/index.jsp.oo?#center=" + u.getCurrentLocation().getLatitude() + "," + u.getCurrentLocation().getLongitude(); 
String location = "?lat=" + u.getCurrentLocation().getLatitude() + "&lng=" + u.getCurrentLocation().getLongitude();
%>
<a href='#' onclick='window.location="<c:url value="/m/user/search/index.jsp.oo"/><%=location%>"' data-theme="b" data-role='button'>friends close to <c:if test="${me.id == user.id }">me</c:if><c:if test="${me.id != user.id }">${user.name }</c:if></a>
<%} %>

<c:if test="${me.id == user.id }">
	<h3>${user.name }</h3>
</c:if>
<c:if test="${me.id != user.id }">
	<h3>${user.name }
	</h3>
</c:if>

	<div style="float:right;text-align:center;margin-top:-48px;">
	<img <c:if test="${me.id == user.id }">onclick="Device.pickupPhotoFromGallery(80, 80, '<o:url value="/user/icon"/>', null, 'iconUploadSuccessCallback', 'iconUploadErrorCallback')"</c:if> 
		src='<g:user-icon user="${user}"/>' 
		style="width:80px;padding:0 5px 0 0;" />
	<c:if test="${me.id == user.id }">
	<br/>
	<span style="font-size:0.8em;color:#000;">click to upload</span>
	</c:if>
	</div>

	<c:if test="${me.id != user.id }">
		<div style='float:right;text-align:center;padding:3px;clear:right;'>
			<input type='button' value='talk' onclick='sendMessage(${user.id})' />
		</div>
	</c:if>

<div style='font-size:0.9em;color:#666;margin-top:-12px;float:left;clear:left;'>
<span>${user.gender}, ${user.age} years old</span>
</div>

<% if(u.getCurrentLocation()!=null) { %>
<div style='font-size:0.9em;color:#666;float:left;clear:left;'>
	${user.currentLocation.city }, ${user.currentLocation.state }, ${user.currentLocation.country }
</div>
<% } %>

	<jsp:include page="/dating/my-profile?format=object&var=dims"></jsp:include>
	<c:forEach var="dimvalue" items="${dims.list }">
	<c:if test="${dimvalue.dimension.name == 'Looking For'}">
		<div style='font-size:0.9em;color:#666;float:left;clear:left;'>
		<c:if test="${empty dimvalue.dvalue}">Looking for ${dimvalue.dimvalue }</c:if> 
		<c:if test="${not empty dimvalue.dvalue}">Looking for ${dimvalue.dvalue }</c:if>
		<c:if test="${empty dimvalue.dvalue && empty dimvalue.dimvalue}">N/A</c:if>
		</div>
	</c:if>
	</c:forEach>

<c:if test="${not empty user.interests }">
<div style='font-size:0.9em;color:#666;float:left;clear:left;'>
	Interests: ${user.interests}
</div>
</c:if>
	
<div style="clear:both;height:1px;">&nbsp;</div>
<div data-role="collapsible" data-theme="c" data-collapsed="false">
	<h3>Album</h3>
	<jsp:include page="/user/resources/album/${user.id }?format=object&var=albumlist"></jsp:include>
	<c:if test="${empty albumlist.list }">
		<p>No photo uploaded.</p>
	</c:if>
	
	<c:if test="${not empty albumlist.list }">
		<div style="width;100%">
			<c:forEach var="file" items="${albumlist.list}">
				<div style="float:left;text-align:center;width:80px;height:80px;overflow:hidden;padding:10px;">
					<a href='#' onclick='window.location="<c:url value="/m/user/download-album/index.jsp.oo?fid=${file.id}&uid=${file.user.id}"/>"'>
					<img style="width:80px;" src='<c:url value="/user/download-album/${file.id}?width=80"/>'/>
					</a>
				</div>
			</c:forEach>
		</div>
	</c:if>
	
	<c:if test="${me.id == user.id }">
	<div style="clear:both;height:1px;">&nbsp;</div>
	<div style="float:left" data-inline="true">
		<a href='#' data-role="button" onclick="Device.pickupPhotoFromGallery(0, 0, '<o:url value="/user/album"/>', null, 'iconUploadSuccessCallback', 'iconUploadErrorCallback')">Upload</a>
	</div>
	<p style="clear:both;padding-top:10px;font-size:0.8em;color:#aaa;">If you could not upload photos, 
	please check if you have latest application.
	</p>
	</c:if>
</div>

<div style="clear:both;height:1px;">&nbsp;</div>

<c:if test="${me.id == user.id }">
	<div data-role="collapsible" data-theme="c" data-collapsed="false">
	<h3>My Friends</h3>

	<c:if test="${empty myfriends.list }">
		<p>Empty friend list.</p>
	</c:if>

	<c:if test="${not empty myfriends.list }">
		<div style="width;100%">
			<c:forEach var="friend" items="${myfriends.list}">
				<div style="float:left;text-align:center;">
					<a href='#' onclick='window.location="<c:url value="/m/dating/profile/index.jsp.oo?uid=${friend.id}"/>"'>
					<img src='<g:user-icon user="${friend}"/>'/><br/>
					${friend.name}</a>
				</div>
			</c:forEach>
		</div>
	</c:if>
	</div>
</c:if>

<c:if test="${me.id != user.id && isMyFriend == 'false'}">
<button id="btnAddFriend" data-theme="b" >Add ${user.name } to My Friend List</button>
</c:if>

<script type="text/javascript" >
function iconUploadSuccessCallback() {
	window.location.reload();
}

function iconUploadErrorCallback() {
}

$(document).ready(function(){
    $('#btnAddFriend').click(function() {
    	$.ajax({
    		url : serverBase + "/user/add-friend/${user.id}",
    		type : "POST",
    		headers: {
    			"Content-Type" : "application/json; charset=utf-8",
    			"Accept": "application/json; charset=utf-8"
    		},
    		complete : function(transport) {
    			if (transport.status == 200) {
    			    window.location.reload();
    			} else {
    				alert('Error found.' + transport.responseText);
    			}
    		}
    	});
    });	
});

function sendMessage(userId) {
    window.location = '<c:url value="/m/message/send/index.jsp.oo?to="/>' + userId;
}
<c:if test="${me.id == user.id }">
function checkIcon() {
    <c:if test="${empty user.icon}">
		if(confirm("Upload a photo now?")) {
		    Device.pickupPhotoFromGallery(80, 80, '<o:url value="/user/icon"/>', null, 'iconUploadSuccessCallback', 'iconUploadErrorCallback');
		}    
    </c:if>
}
$(document).ready(function(){
    checkIcon();
});
</c:if>
</script>

<c:if test="${me.superUser}">

<script type="text/javascript" >
function banUser(userid) {
    if (confirm("Are you sure to block this user?")) {
	    var url = '<c:url value="/user/ban"/>/' + userid;
		$.ajax({
			url : url,
			type : "POST",
			contentType:"application/json; charset=utf-8",
			complete : function(transport) {
				if (transport.status == 200) {
				    Device.back();
				} else {
				    alert("Error found. Please try again later.\n" + transport.responseText);
				}
			}
		});
    }
    return false;
}
function unbanUser(userid) {
    if (confirm("Are you sure to open this user?")) {
	    var url = '<c:url value="/user/unban"/>/' + userid;
		$.ajax({
			url : url,
			type : "POST",
			contentType:"application/json; charset=utf-8",
			complete : function(transport) {
				if (transport.status == 200) {
				    window.location.reload();
				} else {
				    alert("Error found. Please try again later.\n" + transport.responseText);
				}
			}
		});
    }
    return false;
}
function deleteUser(userid) {
    if (confirm("Are you sure to delete this user?")) {
	    var url = '<c:url value="/user/delete"/>/' + userid;
		$.ajax({
			url : url,
			type : "POST",
			contentType:"application/json; charset=utf-8",
			complete : function(transport) {
				if (transport.status == 200) {
				    history.back();
				} else {
				    alert("Error found. Please try again later.\n" + transport.responseText);
				}
			}
		});
    }
    return false;
}
</script>
<c:if test="${user.role != 'BADGUY' }">
<div style="clear:both;"><button onclick='return banUser(${user.id})' data-theme="a">Block ${user.name }</button></div>
</c:if>
<c:if test="${user.role == 'BADGUY' }">
<div style="clear:both;"><button onclick='return unbanUser(${user.id})' data-theme="a">Unblock ${user.name }</button></div>
</c:if>
<div style="clear:both;"><button onclick='return deleteUser(${user.id})' data-theme="a">Delete ${user.name }</button></div>
</c:if>
 
