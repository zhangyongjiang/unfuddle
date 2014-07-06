<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<jsp:include page="/dating/my-profile?format=object&var=dims"></jsp:include>
<h3>Edit</h3>

<form onsubmit="return changeProfile();">

<c:forEach var="dimvalue" items="${dims.list }">
	<div data-role="fieldcontain">
		<label for="dim-${dimvalue.dimension.id }" class="select">${dimvalue.dimension.name }:</label>
		<c:if test="${empty dimvalue.dimension.values}">
		    <input type="text" name="dim-${dimvalue.dimension.id }" id="dim-${dimvalue.dimension.id }" value="${dimvalue.dimvalue}"  data-theme="d" />
		</c:if>
		<c:if test="${not empty dimvalue.dimension.values}">
			<select name="dim-${dimvalue.dimension.id }" id="dim-${dimvalue.dimension.id }" data-theme="c">
				<c:forEach var="dv" items="${dimvalue.dimension.values}">
					<c:if test="${(not empty dimvalue.dimvalue) && (not empty dv.dimvalue) && dimvalue.dimvalue == dv.dimvalue }">
						<option selected value="${dv.dimvalue }">${dv.dvalue }</option>
					</c:if>
					<c:if test="${not ((not empty dimvalue.dimvalue) && (not empty dv.dimvalue) && dimvalue.dimvalue == dv.dimvalue) }">
						<option value="${dv.dimvalue }">${dv.dvalue }</option>
					</c:if>
				</c:forEach>
			</select>
		</c:if>
</div>
</c:forEach>

<div class="ui-body ui-body-b">
	<fieldset class="ui-grid-a">
			<div class="ui-block-a"><button id="cancel" data-theme="a">Cancel</button></div>
			<div class="ui-block-b"><button type="submit" data-theme="b">Save</button></div>
	</fieldset>
</div>

</form>

<script type="text/javascript" >
$(document).ready(function(){
	$('#cancel').click(function(){
		history.back();
	});
});

function changeProfile() {
	var req = {
		list: [
		   	<c:forEach var="dimvalue" items="${dims.list }" varStatus="vs"> {
		   		dimvalue:$('#dim-${dimvalue.dimension.id }').val(),
		   		dimension: {id: '${dimvalue.dimension.id }'}
		   	}<c:if test="${!vs.last}">,</c:if></c:forEach>
		]
	}
	
	var json = JSON.stringify(req);
	$.ajax({
		url : serverBase + "/user/dimensions",
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

$(document).ready(function(){
	$('#phone').attr("disabled", "disabled");

});
</script>