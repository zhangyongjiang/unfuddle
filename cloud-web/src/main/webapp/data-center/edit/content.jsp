<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%
	String dataCenterId = request.getParameter("dataCenterId");
	String url = "/data-center/details?format=object&var=it&dataCenterId=" + dataCenterId;
	request.getRequestDispatcher(url).include(request, response);
%>

<div class="page-header">Edit Data Center</div>
<div style="margin:-18px 0 32px;;font-weight:normal;">${it.name}</div>

<form method="post" onSubmit="return updateDataCenter()">
<table style="border-left: solid 1px #fff;">
	<o:text-input-tr id="name" label="Name" value="${it.name }"></o:text-input-tr>
	<o:textarea-tr   id="description" label="Description">${it.description }</o:textarea-tr>
	<o:submit-tr value="Update" cancel="window.history.back()"></o:submit-tr>
</table>
</form>

<script type="text/javascript">
function updateDataCenter() {
	var req = {
		id: "${it.id}",
		name: $("#name").val(),
		description: $("#description").val()
	};
	
    var json = JSON.stringify(req);
    $.ajax({
            url : base + "/data-center/update",
            type : "POST",
            data : json,
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            complete : function(transport) {
                    if (transport.status == 200) {
                            window.location = base + "/data-center/details/index.jsp.oo?dataCenterId=${it.id}";
                    } else {
                            alert('Error: ' + transport.status + ", "
                                            + transport.responseText);
                    }
            }
    });

    return false;
}
</script>

