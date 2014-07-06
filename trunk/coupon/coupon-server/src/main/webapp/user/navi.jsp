<%@page import="java.util.Properties"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

Welcome ${me.firstName } ${me.lastName }
<div style="height: 1px;clear:both;">&nbsp;</div>
<!-- button style="margin:5px;padding:5px;width:160px;height:36px;" onclick="addStore()">Add Store</button>
<div style="height: 1px;clear:both;">&nbsp;</div -->
<button style="margin:5px;padding:5px;width:160px;height:36px;" onclick="logout()">Logout</button>

<script type="text/javascript">
function addStore() {
	window.location = serverBase + "/user/store/add/index.jsp.oo";
}

function logout() {
	var url = serverBase + "/ws/user/logout";
	$.ajax({
	  url:url,
	  type:"POST",
	  contentType:"application/json; charset=utf-8",
	  dataType:"json",
	  complete: function(transport) {
		     if(transport.status == 200) {
		         self.location = '<c:url value="/user/login/index.jsp.oo"/>';
		     } else {
		         alert('Error: ' + transport.status + ", " + transport.responseText);
		     }
		  }
	});

}
</script>
