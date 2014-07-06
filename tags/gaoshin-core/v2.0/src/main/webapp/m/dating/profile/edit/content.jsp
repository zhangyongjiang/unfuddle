<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<h3>Edit</h3>

<form onsubmit="return changeProfile();">
<jsp:include page="/user/profile?format=object&var=user"></jsp:include>

<div style="margin-bottom:20px;">
    <label for="name">Name:</label>
    <input type="text" name="name" id="name" value="${user.name }"  data-theme="d" />
</div>

<div style="margin-bottom:20px;">
    <label for="phone">Phone:</label>
    <input type="text" name="phone" id="phone" value="${user.phone}"  data-theme="c" />
</div>

<div style="margin-bottom:20px;">
    <label for="interests">One Line About Me:</label>
    <input type="text" name="interests" id="interests" value="${user.interests}"  data-theme="d" />
</div>

<div style="margin-bottom:20px;">
	<label for="introduction">Self Introduction:</label>
	<textarea name="introduction" id="introduction" data-theme="d" style="height:100px;">${user.introduction }</textarea>
</div>

<div class="ui-body ui-body-b">
	<fieldset class="ui-grid-a">
			<div class="ui-block-a"><a href='#' data-role='button' id="cancel" data-theme="a" onclick="history.back()">Cancel</a></div>
			<div class="ui-block-b"><button type="submit" data-theme="b">Save</button></div>
	</fieldset>
</div>

</form>

<script type="text/javascript" >
function changeProfile() {
	var name = $('#name').val();
	if(name==null || name.trim().length==0) {
		alert("Name is required.");
		return false;
	}
	
	var interests = $('#interests').val();
	if(interests==null || interests.trim().length==0) {
		alert("One Line About Me is required.");
		return false;
	}
	
	var req = {
		name: name,
		phone: $('#phone').val(),
		interests: interests,
		introduction: $('#introduction').val()
	}
	
	var json = JSON.stringify(req);
	Device.showLoadingDialog();
	$.ajax({
		url : serverBase + "/user/update",
		type : "POST",
		data : json,
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		complete : function(transport) {
			Device.hideLoadingDialog();
			if (transport.status == 200) {
				window.location = '<c:url value="/m/dating/profile/index.jsp.oo"/>';
			} else {
				alert('Error: ' + transport.status + ", "
						+ transport.responseText);
			}
		}
	});
	
	return false;
}

$(document).ready(function(){
	$('#phone').attr("disabled", "disabled");

});
</script>