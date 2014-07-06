<%@page import="com.gaoshin.dating.DatingProfile"%>
<%@page import="java.util.Calendar"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<script type="text/javascript">
$(document).ready(function(){
    $('#kgslider').change(function(){
        var oldValue = $('#lbslider').val() + "";
        var newValue = Math.round($('#kgslider').val() * 2.205) + "";
        if(oldValue == newValue)
            return;
        $('#lbslider').val(Number(newValue));
        $('#lbslider').slider('refresh');
    });
    $('#lbslider').change(function(){
        var oldValue = $('#kgslider').val() + "";
        var newValue = Math.round($('#lbslider').val() / 2.205) + "";
        if(oldValue == newValue)
            return;
        $('#kgslider').val(Number(newValue));
        refreshSlider(1);
    });
});

counter = 0;
function refreshSlider(inc) {
    counter += inc;
    if(counter<=0) {
        $('#kgslider').slider('refresh');
        return;
    }
    setTimeout("refreshSlider(-1)", 200);
}

function change() {
    var req = {
        value : $('#kgslider').val()
    };
	var json = JSON.stringify(req);
	$.ajax({
		url : serverBase + "/dating/profile/edit-weight",
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

<jsp:include page="/dating/profile/${me.id}?format=object&var=profile"></jsp:include>
<h3>My Weight</h3>

<% 
	DatingProfile profile = (DatingProfile)request.getAttribute("profile");
	int centimeter = profile.getWeight();
	int inches = (int)((float)profile.getWeight() * 2.205);
%>

<div data-role="fieldcontain">
	<div style="width:100%;text-align:center;margin-bottom:8px;">Kilograms</div>
 	<input type="range" name="cm" id="kgslider" value="<%=centimeter %>" min="0" max="150"  />
</div>

<div data-role="fieldcontain">
	<div style="width:100%;text-align:center;margin-bottom:8px;">Pounds</div>
 	<input type="range" name="inch" id="lbslider" value="<%=inches%>" min="0" max="330"  />
</div>

<div class="ui-body ui-body-b" style='margin-top:0px;'>
	<fieldset class="ui-grid-a">
			<div class="ui-block-a"><a href='#' data-role='button' onclick='history.back()' id="cancel" data-theme="a">Cancel</a></div>
			<div class="ui-block-b"><button data-theme="b" onclick='change()'>Save</button></div>
	</fieldset>
</div>

