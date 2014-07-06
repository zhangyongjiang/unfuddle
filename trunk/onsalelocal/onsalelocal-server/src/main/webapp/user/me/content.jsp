<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>

<osl:ws path="/ws/user/me?format=json"/>
<h3>Profile</h3>

<c:if test="${not empty me.img }">
    <div><img style="width: 200px;" src='${me.img }'/></div>
</c:if>
    <div><a href='<c:url value="/user/change-avatar/index.jsp.oo"/>'><span style="color:#aaa;font-size:0.7em;">Upload Avatar</span></a>
    </div>
    
<table>
<o:tr-label-value label="First Name" value="${me.firstName }"/>
<o:tr-label-value label="Last Name" value="${me.lastName }"/>
<o:tr-label-value label="Email" value="${me.login }"/>
<o:tr-label-value label="Zipcode" id="zipcode" value="${me.zipcode}"/>
<o:tr-label-value label="Gender" id="gender" value="${me.gender}"/>
<o:tr-label-value label="Notification" >
    <c:if test="${me.noti eq 'Enable' }">
        <span style='margin-right:16px;'><input type="radio" name="noti" checked value="Enable">Enable</span>
        <span style='margin-right:16px;'><input type="radio" name="noti" value="Disable">Disable</span>
    </c:if>
    <c:if test="${me.noti eq 'Disable' }">
        <span style='margin-right:16px;'><input type="radio" name="noti" value="Enable">Enable</span>
        <span style='margin-right:16px;'><input type="radio" name="noti" checked value="Disable">Disable</span>
    </c:if>
</o:tr-label-value>
</table>
<div style="border:solid 1px #ccc;margin:6px;padding:4px;float:left;">
    <a href='<c:url value="/user/change-profile/index.jsp.oo"/>'>Change Profile</a>
</div>
<div style="border:solid 1px #ccc;margin:6px;padding:4px;float:left;">
    <a href='<c:url value="/user/followers/index.jsp.oo?userId="/>${me.id}'>Followers</a>
</div>
<div style="border:solid 1px #ccc;margin:6px;padding:4px;float:left;">
    <a href='<c:url value="/user/followings/index.jsp.oo?userId="/>${me.id}'>Followings</a>
</div>

<script type="text/javascript">
$(document).ready(function(){
	$('input:radio[name=noti]').change(function() {
		  var val = $('input:radio[name=noti]:checked').val();
          var url = serverBase + "/ws/user/notification/disable";
		  if(val == 'Enable') {
			  url = serverBase + "/ws/user/notification/enable";
		  }
		    $.ajax({
		          url: url,
		          type:"POST",
		          contentType:"application/json; charset=utf-8",
		          dataType:"json",
		          complete: function(transport) {
		              }
		        });
		});
});

</script>
