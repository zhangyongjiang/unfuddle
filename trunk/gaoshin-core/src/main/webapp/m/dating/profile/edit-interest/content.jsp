<%@page import="com.gaoshin.dating.DimensionLanguage"%>
<%@page import="com.gaoshin.dating.DatingProfile"%>
<%@page import="com.gaoshin.dating.DimensionJob"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<jsp:include page="/dating/profile/${me.id}?format=object&var=profile"></jsp:include>

<h3>My Interests</h3>

<div style="margin-bottom:20px;">
    <label for="interest0">Interest - 1:</label>
    <input type="text" name="interest0" id="interest0" value="${profile.interest0}"  data-theme="d" />
</div>

<div style="margin-bottom:20px;">
    <label for="interest1">Interest - 2:</label>
    <input type="text" name="interest1" id="interest1" value="${profile.interest1}"  data-theme="d" />
</div>

<div style="margin-bottom:20px;">
    <label for="interest2">Interest - 3:</label>
    <input type="text" name="interest2" id="interest2" value="${profile.interest2}"  data-theme="d" />
</div>

<div class="ui-body ui-body-b" style='margin-top:12px;'>
	<fieldset class="ui-grid-a">
			<div class="ui-block-a"><a href='#' data-role='button' onclick='history.back()' id="cancel" data-theme="a">Cancel</a></div>
			<div class="ui-block-b"><button onclick='save()' data-theme="b">Save</button></div>
	</fieldset>
</div>

<script type="text/javascript" >
function save() {
    change(0);
}

function change(interestid) {
    var value = $('#interest'+interestid).val();
    var req = {
        value : value
    };
	var json = JSON.stringify(req);
	$.ajax({
		url : serverBase + "/dating/profile/edit-interest" + interestid,
		type : "POST",
		data : json,
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		complete : function(transport) {
			if (transport.status == 200) {
			    if(interestid == 0) {
			        change(1);
			    }else if(interestid == 1) {
				        change(2);
			    } else if(interestid == 2) {
					window.location = '<c:url value="/m/dating/profile/index.jsp.oo"/>';
			    }
			} else {
				alert('Error: ' + transport.status + ", "
						+ transport.responseText);
			}
		}
	});
	
	return false;
}

</script>