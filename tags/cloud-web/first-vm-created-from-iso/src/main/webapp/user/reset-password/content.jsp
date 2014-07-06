<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<div class="page-header">Email Password</div>

<form onSubmit="return emailPassword()">
  <table>
	<o:text-input-tr id="email" label="Email"></o:text-input-tr>
	<o:submit-tr value="go" cancel="window.history.back()"></o:submit-tr>
	<tr><td></td><td>
		<div style="margin-top:20px;">Already have account? <a href='<c:url value="/user/signin/index.jsp.oo"/>'>Sign In</a></div>
		<div style="margin-top:20px;">No account yet? <a href='<c:url value="/user/signup/index.jsp.oo"/>'>Sign Up</a></div>
	</td></tr>
  </table>
</form>

<script type="text/javascript">
function emailPassword() {
	return false;
}
</script>