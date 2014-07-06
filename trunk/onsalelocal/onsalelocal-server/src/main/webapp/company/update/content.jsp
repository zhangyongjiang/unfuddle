<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<%
    String param = request.getQueryString();
    String url = "/ws/company?format=object&" + (param == null ? "" : param);;
    request.getRequestDispatcher(url).include(request, response);
%>

<div style="width:100%;">
<h3>Update Company</h3>
</div>

<form onsubmit="return updateCompany();">
<input type="hidden" id="id" value="${it.id }"/>
<table>
<o:text-input-tr id="name"  value="${it.name }"/>
<o:text-input-tr id="web"   value="${it.web }"/>
<o:text-input-tr id="logo"  value="${it.logo }"/>
<o:text-input-tr id="phone" value="${it.phone}"/>
<o:text-input-tr id="email" value="${it.email}"/>
<o:submit-tr value="Update Company"/>
</table>
</form>

<script type="text/javascript">
function updateCompany() {
    var req = {
            id:$('#id').val(),
            name:$('#name').val(),
            web:$('#web').val(),
            logo:$('#logo').val(),
            phone:$('#phone').val(),
            email:$('#email').val(),
        };
	var json = JSON.stringify(req);
	var url = serverBase + "/ws/company/update";
	$.ajax({
		  url: url,
		  type:"POST",
		  data:json,
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  complete: function(transport) {
			     if(transport.status == 200) {
                     var resp = JSON.parse(transport.responseText);
                     var url = serverBase + "/company/details/index.jsp.oo?companyId=" + resp.id;
                     self.location = url;
			     } else {
			         alert('Error: ' + transport.responseText);
			     }
			  }
		});

	return false;
}
</script>
