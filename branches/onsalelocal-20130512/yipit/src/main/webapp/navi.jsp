<%@page import="java.util.Properties"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>


<div style="position:absolute;top:0;left:0;right:0;height:32px;background-color: #bbb; ">
    <div style="float:left;margin:8px;">
        <a href='<c:url value="/index.jsp.oo"/>'>On Sale Local</a>
    </div>
    
<c:if test="${empty me.id }">
    <div style="float:right;margin:8px;">
    	<a href='<c:url value="/user/login/index.jsp.oo"/>'>Login</a>
    </div>
</c:if>

<c:if test="${not empty me.id }">
    <div style="float:right;margin:8px;">
        <a href='javascript:void(0)' onclick="logout()">Logout</a>
    </div>
    <div style="float:right;margin:8px;">
        <a href='<c:url value="/user/index.jsp.oo"/>'>${me.name }</a>
    </div>
</c:if>

</div>

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
		         self.location = '<c:url value="/reward/latest/index.jsp.oo"/>';
		     } else {
		         alert('Error: ' + transport.status + ", " + transport.responseText);
		     }
		  }
	});

}
</script>
