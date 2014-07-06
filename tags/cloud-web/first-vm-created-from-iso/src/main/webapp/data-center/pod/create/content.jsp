<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%
	String dataCenterId = request.getParameter("dataCenterId");
	String url = "/data-center/details?format=object&var=dataCenter&dataCenterId=" + dataCenterId;
	request.getRequestDispatcher(url).include(request, response);
%>

<div class="page-header">Create a Pod</div>

<div style="margin:-18px 0 0px;;font-weight:normal;">in data center <g:data-center-link dataCenter="${dataCenter }"/></div>

<form method="post" onSubmit="return createPod()">
<table style="border-left: solid 1px #fff;">
	<o:text-input-tr id="name" label="Name"></o:text-input-tr>
	<o:textarea-tr   id="description" label="Description"></o:textarea-tr>
	<o:submit-tr value="Create" cancel="window.history.back()"></o:submit-tr>
</table>
</form>

<script type="text/javascript">
function createPod() {
	var req = {
		dataCenterId: "<%=dataCenterId%>",
		name: $("#name").val(),
		description: $("#description").val()
	};
	
    var json = JSON.stringify(req);
    $.ajax({
            url : base + "/data-center/pod/create",
            type : "POST",
            data : json,
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            complete : function(transport) {
                var result = JSON.parse(transport.responseText);
                    if (transport.status == 200) {
                            window.location = base + "/data-center/pod/details/index.jsp.oo?podId=" + result.id;
                    } else {
                            alert('Error: ' + transport.status + ", "
                                            + transport.responseText);
                    }
            }
    });

    return false;
}
</script>

