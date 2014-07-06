<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<div class="page-header">Sign Up</div>

<form onSubmit="return signup();">
  <table>
	<o:text-input-tr id="username" label="User"></o:text-input-tr>
	<o:password-input-tr id="password" label="Password"/>
	<o:password-input-tr id="password1" label="Password Confirm"/>
	<o:text-input-tr id="displayName" label="Display Name"></o:text-input-tr>
	<o:text-input-tr id="email" label="Email"></o:text-input-tr>
	<o:submit-tr value="Sign Up" cancel="window.history.back()"></o:submit-tr>
	<tr><td></td><td>
		<div style="margin-top:20px;">Already have account? <a href='<c:url value="/user/signin/index.jsp.oo"/>'>Sign In</a></div>
		<div style="margin-top:10px;">Forgot password? <a href='<c:url value="/user/reset-password/index.jsp.oo"/>'>Retrieve Password</a></div>
	</td></tr>
  </table>
</form>

<script type="text/javascript">
function signup() {
	if($("#username").val().trim().length == 0) {
		alert("invalid user name");
		return false;
	}
	
	if($("#password").val().trim().length == 0 || $("#password1").val().trim().length == 0 || $("#password").val() != $("#password1").val()) {
		alert("invalid password");
		return false;
	}
	
	if($("#displayName").val().trim().length == 0) {
		alert("invalid display name");
		return false;
	}
	
	if($("#email").val().trim().length == 0) {
		alert("invalid email");
		return false;
	}
	
	var req = {
		username: $("#username").val(),
		password: $("#password").val(),
		email: $("#email").val(),
		displayName: $("#displayName").val()
	};
	
    var json = JSON.stringify(req);
    $.ajax({
            url : base + "/user/signup",
            type : "POST",
            data : json,
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            complete : function(transport) {
                var result = JSON.parse(transport.responseText);
                    if (transport.status == 200) {
                            window.location = base + "/user/signin/index.jsp.oo";
                    } else {
                            alert('Error: ' + transport.status + ", "
                                            + transport.responseText);
                    }
            }
    });

    return false;
}
</script>