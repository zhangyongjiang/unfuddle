<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<h3>Logout</h3>

<form onsubmit="return logout();">
<button type="submit">Logout</button>
</form>

<script type="text/javascript">
function logout() {
	$.ajax({
		  url:serverBase + "/user/logout",
		  type:"POST",
		  data:" ",
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  complete: function(transport) {
			     if(transport.status == 200) {
			         window.location = '<c:url value="/m/home/index.jsp.oo"/>';
			     } else {
			         alert('Error: ' + transport.status + ", " + transport.responseText);
			     }
			  }
		});

	return false;
}
</script>