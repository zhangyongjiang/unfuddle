<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<h3>Recover Password</h3>

<form onsubmit="return recoverPassword();">
<table>
<o:text-input-tr label="Email" id="login"/>
<o:submit-tr value="Email Password"/>
</table>
</form>

<div style="margin-top:32px;">
No account yet? <a href='#' onclick="self.location='<c:url value="/user/register/main.jsp.oo"/>'">Sign Up...</a>
</div>

<div style="clear:both;margin-top:10px;"><span style="font-size:0.8em;">Already got account?</span> <a href="#" onclick='window.location="<c:url value="/user/login/index.jsp.oo"/>"'>Sign In</a></div>

<script type="text/javascript">
function recoverPassword() {
	var url = serverBase + "/ws/user/reset-password/" + $('#login').val();
	$.ajax({
		  url:url,
		  type:"POST",
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  complete: function(transport) {
			     if(transport.status == 200) {
				     window.location = serverBase + "/index.jsp.oo";
			     } else {
			         alert('Error: ' + transport.status + ", " + transport.responseText);
			     }
			  }
		});

	return false;
}
</script>
