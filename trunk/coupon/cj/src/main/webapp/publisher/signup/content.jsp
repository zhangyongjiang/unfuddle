<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<div style="width:100%;">
<h3>Sign Up</h3>
</div>

<form onsubmit="return signup();">
<table style="width:100%">
<o:text-input-tr label="Company Name" id="name"/>
<o:text-input-tr label="User ID" id="login"/>
<o:password-input-tr label="Password" id="password"/>
<o:password-input-tr label="Password Confirm" id="password2"/>
<o:text-input-tr label="Icon" id="icon"/>
<o:textarea-tr id="description" label="Description"></o:textarea-tr>
<o:submit-tr value="Sign Up"/>
</table>
</form>

<div style="clear:both;margin-top:32px;"><span style="font-size:0.8em;">Already got account?</span> <a href="#" onclick='window.location="<c:url value="/user/login/index.jsp.oo"/>"'>Sign In</a></div>

<script type="text/javascript">
function signup() {
	var login = $("#login").val();
	if(login == null || login.trim().length == 0) {
		alert("All filelds are required. Please check login field.");
		return false;
	}
	
	var password = $("#password").val();
	if(password == null || password.trim().length == 0) {
		alert("All filelds are required. Please check password field.");
		return false;
	}
	
	var password2 = $("#password2").val();
	if(password2 == null || password2.trim().length == 0) {
		alert("All filelds are required. Please check password confirm field.");
		return false;
	}

	if(password != password2) {
		alert("Password and password confirm doesn't match.");
		return false;
	}
	
		var req = {
            name: $('#name').val(),
			login: login,
			password:password,
            icon: $('#icon').val(),
            description:$('#description').val(),
		};
		var json = JSON.stringify(req);
		var url = serverBase + "/ws/user/register";
		$.ajax({
			  url: url,
			  type:"POST",
			  data:json,
			  contentType:"application/json; charset=utf-8",
			  dataType:"json",
			  complete: function(transport) {
				     if(transport.status == 200) {
				         self.location = '<c:url value="/user/login/index.jsp.oo"/>';
				     } else {
				         alert('Error: ' + transport.responseText);
				     }
				  }
			});

	return false;
}
</script>