<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<div class="page-header">Add a Host</div>

<form method="post" onSubmit="return addHost()">
<table style="border-left: solid 1px #fff;">
	<o:text-input-tr id="name" label="Name"></o:text-input-tr>
	<o:text-input-tr id="url" label="URL"></o:text-input-tr>
	<o:text-input-tr id="user" label="User"></o:text-input-tr>
	<o:password-input-tr id="password" label="Password"></o:password-input-tr>
	<o:submit-tr value="Add" cancel="window.history.back()"></o:submit-tr>
</table>
</form>

<script type="text/javascript">
function addHost() {
	var req = {
		name: $("#name").val(),
		url: $("#url").val(),
		user: $("#user").val(),
		password: $("#password").val()
	};
	
    var json = JSON.stringify(req);
    $.ajax({
            url : base + "/xen/host/add",
            type : "POST",
            data : json,
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            complete : function(transport) {
                    if (transport.status == 200) {
                        var host = JSON.parse(transport.responseText);
                        window.location = base + "/xen/host/details/index.jsp.oo?hostId=" + host.id;
                    } else {
                        alert('Error: ' + transport.status + ", "
                                        + transport.responseText);
                    }
            }
    });

    return false;
}
</script>
