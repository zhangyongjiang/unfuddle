<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>


<div style="width:100%;">
<h3>Sign Up</h3>
</div>

<form onsubmit="return register();">
<table>
<o:text-input-tr label="User ID" id="login"/>
<o:password-input-tr label="Password" id="password"/>
<o:text-input-tr label="First Name" id="firstName"/>
<o:text-input-tr label="Last Name" id="lastName"/>
<o:text-input-tr label="Birth Year" id="birthYear"/>
<o:tr label="Gender">
	<select id="gender" name="gender">
		<option value="Man">Man</option>
		<option value="Woman">Woman</option>
	</select>
</o:tr>
<o:submit-tr value="Sign Up"/>
</table>
</form>

<div style="clear:both;margin-top:32px;"><span style="font-size:0.8em;">Already got account?</span> <a href="#" onclick='window.location="<c:url value="/user/login/index.jsp.oo"/>"'>Sign In</a></div>

<script type="text/javascript">
function register() {
	var login = $("#login").val();
	var password = $("#password").val();
	
	var req = {
		login: login,
		password:password,
		gender:$('#gender').val(),
		firstName:$('#firstName').val(),
		lastName:$('#lastName').val(),
		birthYear:$('#birthYear').val(),
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
			         self.location = '<c:url value="/index.jsp.oo"/>';
			     } else {
			         alert('Error: ' + transport.responseText);
			     }
			  }
		});

	return false;
}
</script>
