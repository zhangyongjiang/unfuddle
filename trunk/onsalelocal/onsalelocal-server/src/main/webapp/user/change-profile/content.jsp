<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<div style="width:100%;">
<h3>Change Profile</h3>
</div>

<form onsubmit="return changeProfile();">
<table>
<o:text-input-tr label="First Name" id="firstName" value="${me.firstName }"/>
<o:text-input-tr label="Last Name" id="lastName" value="${me.lastName }"/>
<o:text-input-tr label="Email" id="login" value="${me.login }"/>
<o:password-input-tr label="Password" id="password"/>
<o:text-input-tr label="Zipcode" id="zipcode" value="${me.zipcode}"/>
<o:submit-tr value="Update Profile"/>
</table>
</form>

<script type="text/javascript">
function changeProfile() {
	var login = $("#login").val();
	var password = $("#password").val();
	
	var req = {
		login: login,
		password:password,
		firstName:$('#firstName').val(),
		lastName:$('#lastName').val(),
		zipcode:$('#zipcode').val(),
	};
	var json = JSON.stringify(req);
	var url = serverBase + "/ws/user/update";
	$.ajax({
		  url: url,
		  type:"POST",
		  data:json,
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  complete: function(transport) {
			     if(transport.status == 200) {
			         self.location = serverBase + "/user/me/index.jsp.oo";
			     } else {
			         alert('Error: ' + transport.responseText);
			     }
			  }
		});

	return false;
}
</script>
