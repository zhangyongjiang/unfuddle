<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<h3>Sign In</h3>

<form onsubmit="return doLogin();">
<table>
<o:text-input-tr label="ID" id="login"/>
<o:password-input-tr label="Password" id="password"/>
<o:submit-tr value="Login"/>
</table>
</form>

<div style="margin-top:32px;">
No account yet? <a href='#' onclick="self.location='<c:url value="/user/register/main.jsp.oo"/>'">Sign Up...</a>
</div>

<script type="text/javascript">
function doLogin() {
	var user = {
		login: $("#login").val(),
		password: $("#password").val(),
	};
	var json = JSON.stringify(user);
	var url = serverBase + "/ws/user/login";
	$.ajax({
		  url:url,
		  type:"POST",
		  data:json,
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  complete: function(transport) {
			     if(transport.status == 200) {
				     if(window.location.href.indexOf("login")==-1)
				         window.location.reload();
				     else
					     window.location = serverBase + "/index.jsp.oo";
			     } else {
			         alert('Error: ' + transport.status + ", " + transport.responseText);
			     }
			  }
		});

	return false;
}
</script>
