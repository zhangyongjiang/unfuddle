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

<h3>Children I Have Now</h3>

<div data-role="fieldcontain" data-theme='c'>
    <fieldset data-role="controlgroup" >
    	<% for (int i=0; i<6; i++) { %>
         	<input type="radio" data-theme='c' name="radio-name" id="radio-choice-<%=i%>" value="<%=i %>" <% if(i==profile.getKids()) {%>checked="checked"<%} %> />
         	<label for="radio-choice-<%=i%>"><%= i %></label>
		<% } %>
    </fieldset>
</div>

<div class="ui-body ui-body-b">
	<fieldset class="ui-grid-a">
			<div class="ui-block-a"><a href='#' data-role='button' onclick='history.back()' id="cancel" data-theme="a">Cancel</a></div>
			<div class="ui-block-b"><button onclick="change()" data-theme="b">Save</button></div>
	</fieldset>
</div>

<script type="text/javascript" >
function change() {
    var gender = $("input[name=radio-name]:checked").val();
    var req = {
        value : gender
    };
	var json = JSON.stringify(req);
	$.ajax({
		url : serverBase + "/dating/profile/edit-num-of-kids",
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