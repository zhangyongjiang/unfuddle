<%@page import="com.gaoshin.dating.DatingProfile"%>
<%@page import="com.gaoshin.dating.DimensionLikeChildren"%>
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

<h3>Like to have children?</h3>

    <fieldset data-role="controlgroup" >
    	<% 	for (DimensionLikeChildren like : DimensionLikeChildren.class.getEnumConstants()) {%>
         	<input type="radio" data-theme='c' name="radio-like" id="<%=like.name() %>" value="<%=like.name() %>" <%if(like.equals(profile.getLikekids())){ %>checked="checked"<%}%> />
         	<label for="<%=like.name() %>"><%=like.getLabel() %></label>
    	<% } %>
    </fieldset>

<div class="ui-body ui-body-b" style='margin-top:12px;'>
	<fieldset class="ui-grid-a">
			<div class="ui-block-a"><a href='#' data-role='button' onclick='history.back()' id="cancel" data-theme="a">Cancel</a></div>
			<div class="ui-block-b"><button onclick='change()' data-theme="b">Save</button></div>
	</fieldset>
</div>

<script type="text/javascript" >
function change() {
    var elem = $("input[name=radio-like]:checked");
    if(elem == null || elem.val() == null || elem.val().length == 0) {
        alert("Please select one");
        return;
    }
    var value = elem.val();
    var req = {
        value : value
    };
	var json = JSON.stringify(req);
	$.ajax({
		url : serverBase + "/dating/profile/edit-like-kids",
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