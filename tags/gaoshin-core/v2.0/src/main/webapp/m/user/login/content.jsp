<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<h3>Sign In</h3>

<form onsubmit="return login();">
	<div data-role="fieldcontain">
	    <label for="name">Name:</label>
	    <input type="text" name="name" id="name" value=""  data-theme='d' />
	</div>
	
	<div data-role="fieldcontain">
	    <label for="password">Password:</label>
	    <input type="password" name="password" id="password" value=""  data-theme='d'/>
	</div>	
	
	<button type="submit" data-theme='b'>Sign In</button>
</form>

<div style="margin-top:32px;">
No account yet? <a href='#' onclick="window.location='<c:url value="/m/user/signup/index.jsp.oo"/>'">Sign Up...</a>
</div>

<script type="text/javascript">
$(document).ready(function(){
//    $('#phone').val(Device.getMyPhoneNumber());
});

function login() {
	var user = {
		name: $("#name").val(),
		password: $("#password").val(),
	};
	var json = JSON.stringify(user);
	
	$.ajax({
		  url:serverBase + "/user/login",
		  type:"POST",
		  data:json,
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  complete: function(transport) {
			     if(transport.status == 200) {
			         window.location = '<c:url value="/m/dating/profile/index.jsp.oo"/>';
			     } else {
			         alert('Error: ' + transport.status + ", " + transport.responseText);
			     }
			  }
		});

	return false;
}
</script>