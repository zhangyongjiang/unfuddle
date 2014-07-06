<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<h3>Edit</h3>

<form onsubmit="return changeProfile();">

<div data-role="fieldcontain">
    <label for="name">Phone:</label>
    <input type="text" name="phone" id="phone" value=""  />
</div>

<div data-role="fieldcontain">
    <label for="password">Password:</label>
    <input type="password" name="password" id="password" value="" />
</div>	

<div data-role="fieldcontain">
    <label for="password">Password Again:</label>
    <input type="password" name="password2" id="password2" value="" />
</div>	

<div data-role="fieldcontain">
	<label for="textarea">Self Introduction:</label>
	<textarea cols="40" rows="8" name="indroduction" id="indroduction"></textarea>
</div>

</form>

<script type="text/javascript" >
$(document).ready(function(){
});

function changeProfile() {
	var req = {
		name : $('#name').val(),
	};
	var json = JSON.stringify(req);
	$.ajax({
		url : serverBase + "/user/update",
		type : "POST",
		data : json,
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		complete : function(transport) {
			if (transport.status == 200) {
				window.location.reload();
			} else {
				alert('Error: ' + transport.status + ", "
						+ transport.responseText);
			}
		}
	});
	return false;
}
</script>