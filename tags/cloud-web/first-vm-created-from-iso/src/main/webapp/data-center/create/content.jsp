<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<div class="page-header">Create a New Data Center</div>

<form method="post" onSubmit="return createDataCenter()">
<table style="border-left: solid 1px #fff;">
	<o:text-input-tr id="name" label="Name"></o:text-input-tr>
	<o:textarea-tr   id="description" label="Description"></o:textarea-tr>
	<o:submit-tr value="Create" cancel="window.history.back()"></o:submit-tr>
</table>
</form>

<script type="text/javascript">
function createDataCenter() {
	var req = {
		name: $("#name").val(),
		description: $("#description").val()
	};
	
    var json = JSON.stringify(req);
    $.ajax({
            url : base + "/data-center/create",
            type : "POST",
            data : json,
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            complete : function(transport) {
                var result = JSON.parse(transport.responseText);
                    if (transport.status == 200) {
                            window.location = base + "/data-center/details/index.jsp.oo?dataCenterId=" + result.id;
                    } else {
                            alert('Error: ' + transport.status + ", "
                                            + transport.responseText);
                    }
            }
    });

    return false;
}
</script>

