<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<jsp:include page="/dating/dimensions?format=object&var=dims"></jsp:include>
<h3>Search</h3>

<form onsubmit="return search();">

<div style="margin-bottom:10px;">
	<label for="miles" class="select">within:</label>
	<select name="miles" id="miles" data-theme="d" >
		<option value="1" >1 Mile</option>
		<option value="15" selected>15 Miles</option>
		<option value="30" >30 Miles</option>
		<option value="50" >50 Miles</option>
		<option value="100" >100 Miles</option>
	</select>
</div>

<jsp:include page="/location/my?format=object&var=mylocs"></jsp:include>
<div style="margin-bottom:10px;">
	<label for="location" class="select">of my location:</label>
	<select name="location" id="location" data-theme="d" >
		<c:forEach var="loc" items="${mylocs.list }">
			<option value="lat=${loc.latitude}&lng=${loc.longitude}&addr=${loc.address }+${loc.city }+${loc.state }+${loc.country}" >${loc.address } ${loc.city } ${loc.state }</option>
		</c:forEach>
	</select>
</div>

<c:forEach var="dimension" items="${dims.list }">
	<div style="margin-bottom:10px;">
		<label for="dim-${dimension.id }" class="select">${dimension.name }:</label>
		<c:if test="${empty dimension.values}">
		    <input type="text" name="dim-${dimension.id }" id="dim-${dimension.id }" value=""  data-theme="d" />
		</c:if>
		<c:if test="${not empty dimension.values}">
			<select name="dim-${dimension.id }" id="dim-${dimension.id }" data-theme="c">
				<option value="">Any</option>
				<c:forEach var="dv" items="${dimension.values}">
					<option value="${dv.dimvalue }">${dv.dvalue }</option>
				</c:forEach>
			</select>
		</c:if>
</div>
</c:forEach>

<div class="ui-body ui-body-b">
	<fieldset class="ui-grid-a">
			<div class="ui-block-a"><a href='javascript:history.back()' data-role='button' data-theme="a">Cancel</a></div>
			<div class="ui-block-b"><button type="submit" data-theme="b">Submit</button></div>
	</fieldset>
</div>

</form>

<script type="text/javascript" >
$(document).ready(function(){
});

function search() {
	var req = {
		list: [
		   	<c:forEach var="dimension" items="${dims.list }" varStatus="vs"> {
		   		dimvalue:$('#dim-${dimension.id }').val(),
		   		dimension: {id: '${dimension.id }'}
		   	}<c:if test="${!vs.last}">,</c:if></c:forEach>
		]
	}
	
	var url = "/user/search?miles=" + $('#miles').val() + "&" + $('#location').val();
	window.location = url;
	
	return false;
}
</script>