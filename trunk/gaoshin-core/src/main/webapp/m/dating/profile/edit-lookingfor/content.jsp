<%@page import="com.gaoshin.dating.DimensionLookingFor"%>
<%@page import="com.gaoshin.dating.DatingProfile"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<jsp:include page="/dating/profile/${me.id}?format=object&var=profile"></jsp:include>
<% 
	DatingProfile profile = (DatingProfile)request.getAttribute("profile");
%>
<h3>Looking for?</h3>

<form onsubmit="return go();">

<div data-role="fieldcontain" data-theme='c'>
    <fieldset data-role="controlgroup" >
    	<% 	for (DimensionLookingFor job : DimensionLookingFor.class.getEnumConstants()) {%>
         	<input type="radio" data-theme='c' name="radio-name" id="<%=job.name() %>" value="<%=job.name() %>" <%if(job.equals(profile.getLookingFor())){ %>checked="checked"<%}%> />
         	<label for="<%=job.name() %>"><%=job.getLabel() %></label>
    	<% } %>
    </fieldset>
</div>

<div class="ui-body ui-body-b" style='margin-top:0px;'>
	<fieldset class="ui-grid-a">
			<div class="ui-block-a"><a href='#' data-role='button' onclick='history.back()' id="cancel" data-theme="a">Cancel</a></div>
			<div class="ui-block-b"><button type="submit" data-theme="b">Save</button></div>
	</fieldset>
</div>

</form>

<script type="text/javascript" >
function go() {
    var value = $("input[name=radio-name]:checked").val();
    var req = {
        value : value
    };
	var json = JSON.stringify(req);
	$.ajax({
		url : serverBase + "/dating/profile/edit-lookingfor",
		type : "POST",
		data : json,
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		complete : function(transport) {
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

</script>