<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<div style="float:left;">
<h3>Create Company</h3>

<form id="companyForm" method="post" onSubmit="return createCompany();">
<table>
    <o:text-input-tr id="name"></o:text-input-tr>
    <o:text-input-tr id="web"></o:text-input-tr>
    <o:text-input-tr id="logo"></o:text-input-tr>
    <o:text-input-tr id="phone"></o:text-input-tr>
    <o:text-input-tr id="email"></o:text-input-tr>
    <o:tr-label-value><input type="button" value="submit" onclick="createCompany()"/></o:tr-label-value>
</table>
</form>
</div>

<script type="text/javascript">
function createCompany() {
    var req = {
        name:$('#name').val(),
        web:$('#web').val(),
        logo:$('#logo').val(),
        phone:$('#phone').val(),
        email:$('#email').val(),
    };
    var json = JSON.stringify(req);
    var url = serverBase + "/ws/company/create";
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
