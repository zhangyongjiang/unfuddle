<%@page import="java.util.Properties"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<div style="margin:8px;">
<c:if test="${empty me.id }">
<a href='<c:url value="/index.jsp"/>'>Guest</a>
</c:if>
<c:if test="${not empty me.id }">
<a href='<c:url value="/index.jsp"/>'>${me.firstName } ${me.lastName }</a>
</c:if>
</div>

<c:if test="${empty me.id }">
	<button style="margin:5px;padding:5px;width:160px;height:36px;" onclick="loginPage()">Login</button>
</c:if>
<c:if test="${not empty me.id }">
	<button style="margin:5px;padding:5px;width:160px;height:36px;" onclick="logout()">Logout</button>
</c:if>

<script type="text/javascript">
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
