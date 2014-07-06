<%@page import="com.gaoshin.onsalelist.bean.UserDetails"%>
<%@page import="com.gaoshin.onsalelist.bean.ApplicationDetails"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<%
String userId = request.getParameter("userId");
String applicationId = request.getParameter("applicationId");
if(applicationId != null) {
    request.getRequestDispatcher("/ws/user/application-details?format=object&var=app&applicationId=" + applicationId).include(request, response);
    ApplicationDetails app = (ApplicationDetails)request.getAttribute("app");
    userId = app.getUserId();
}
else {
    ApplicationDetails app = new ApplicationDetails();
    request.setAttribute("app", app);
}
%>

<div style="width:100%;">
<h3>
<c:if test="${empty app.id }">Add Application</c:if>
<c:if test="${not empty app.id }">Edit Application</c:if>
</h3>
</div>

<form onsubmit="return insertOrUpdate();">
<input type="hidden" name="applicationId" id="applicationId" value='<o:context name="applicationId"/>'/>
<table style="width:100%">
<o:text-input-tr label="Title" id="name" value="${app.name }"/>
<o:tr label="Platform">
    <select id="type" name="type">
        <option value="Android" <c:if test="${app.type == 'Android' }">selected</c:if> >Android</option>
        <option value="Iphone" <c:if test="${app.type == 'Iphone' }">selected</c:if> >iPhone</option>
        <option value="Windows" <c:if test="${app.type == 'Windows' }">selected</c:if> >Windows</option>
    </select>
</o:tr>
<o:text-input-tr label="Market ID" id="marketId" value="${app.marketId}"/>
<o:text-input-tr label="Icon" id="icon" value="${app.icon}"/>
<o:text-input-tr label="Image" id="image" value="${app.image}"/>
<o:tr label="Description">
    <textarea id="description" name="description" style="width:80%;height:240px;">${app.description}</textarea>
</o:tr>
<c:if test="${empty app.id }"><o:submit-tr value="Add"/></c:if>
<c:if test="${not empty app.id }"><o:submit-tr value="Update"/></c:if>
</table>
</form>

<script type="text/javascript">
function insertOrUpdate() {
	<c:if test="${empty app.id }">insert();</c:if>
	<c:if test="${not empty app.id }">update();</c:if>
}

function insert() {
	var req = {
		name: $('#name').val(),
		type: $('#type').val(),
		marketId: $('#marketId').val(),
		icon: $('#icon').val(),
		image: $('#image').val(),
		description: $('#description').val()
	};
	var json = JSON.stringify(req);
	var url = serverBase + "/ws/user/add-application";
	$.ajax({
		  url: url,
		  type:"POST",
		  data:json,
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  complete: function(transport) {
			     if(transport.status == 200) {
                     var app = JSON.parse(transport.responseText);
			         self.location = serverBase + '/user/index.jsp.oo';
			     } else {
			         alert('Error: ' + transport.responseText);
			     }
			  }
		});

	return false;
}

function update() {
    var req = {
            id: $('#applicationId').val(),
			name: $('#name').val(),
			type: $('#type').val(),
			marketId: $('#marketId').val(),
			icon: $('#icon').val(),
			image: $('#image').val(),
			description: $('#description').val()
    };
    var json = JSON.stringify(req);
    var url = serverBase + "/ws/user/update-application";
    $.ajax({
          url: url,
          type:"POST",
          data:json,
          contentType:"application/json; charset=utf-8",
          dataType:"json",
          complete: function(transport) {
                 if(transport.status == 200) {
                     var app = JSON.parse(transport.responseText);
                     self.location = serverBase + '/user/index.jsp.oo';
                 } else {
                     alert('Error: ' + transport.responseText);
                 }
              }
        });

    return false;
}
</script>
