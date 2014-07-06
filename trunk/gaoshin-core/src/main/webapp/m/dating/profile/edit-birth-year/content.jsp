<%@page import="com.gaoshin.dating.DatingProfile"%>
<%@page import="java.util.Calendar"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<jsp:include page="/dating/profile/${me.id}?format=object&var=profile"></jsp:include>
<h3>My Birth Year</h3>

<% 
	Calendar cal = Calendar.getInstance();
	int thisYear = cal.get(Calendar.YEAR) - 18;
	DatingProfile profile = (DatingProfile)request.getAttribute("profile");
	for(int i=0; i<60; i++) {
	    thisYear --;
	    String color = "";
	    if(profile.getBirthYear() == thisYear)
	        color = "background-color:#afa;";
%>
<div onclick='change(<%=thisYear %>)' style="<%=color%>float:left;width:48px;padding:5px;margin:1px;border:solid 1px #ccc;text-align:center;"><%= thisYear %></div>
<%	    
	}
%>
<script type="text/javascript" >
function change(newBirthYear) {
    var req = {
        value : newBirthYear
    };
	var json = JSON.stringify(req);
	$.ajax({
		url : serverBase + "/dating/profile/edit-birthyear",
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