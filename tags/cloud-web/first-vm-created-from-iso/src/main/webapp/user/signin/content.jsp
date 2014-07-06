<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<div class="page-header">Sign in</div>

<form name="f" action="<c:url value='/j_spring_security_check'/>" method="POST">
  <table>
	<o:text-input-tr id="j_username" label="User"></o:text-input-tr>
	<o:password-input-tr id="j_password" label="Password"/>
	<o:submit-tr value="Sign In" cancel="window.history.back()"></o:submit-tr>
	<tr><td></td><td>
		<div style="margin-top:20px;">No account yet? <a href='<c:url value="/user/signup/index.jsp.oo"/>'>Sign Up</a></div>
		<div style="margin-top:10px;">Forgot password? <a href='<c:url value="/user/reset-password/index.jsp.oo"/>'>Retrieve Password</a></div>
	</td></tr>
  </table>
</form>