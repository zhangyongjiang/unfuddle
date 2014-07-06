<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<h3 style='color:red;'>Remove Your Account ??</h3>

<button onclick='confirmDeletion()'>Yes, please!</button>

<script type="text/javascript">
function confirmDeletion() {
	$.ajax({
		  url:serverBase + "/user/confirm-deletion",
		  type:"POST",
		  data: " ",
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  complete: function(transport) {
			     if(transport.status == 200) {
			         Device.removeAllCookies();
			         alert("bye");
			         Device.exit();
			     } else {
			         alert('Error: ' + transport.responseText);
			     }
			  }
		});
}
</script>