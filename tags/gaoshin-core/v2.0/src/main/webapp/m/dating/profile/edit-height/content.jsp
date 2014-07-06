<%@page import="com.gaoshin.dating.DatingProfile"%>
<%@page import="java.util.Calendar"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<script type="text/javascript">
function cmchanged(inchid, cmid, feetid) {
    var oldValue = $(inchid).val() + "";
    var newValue = Math.round($(cmid).val() / 2.54) + "";
    if(oldValue == newValue)
        return;
    $(inchid).val(Number(newValue));
    $(inchid).slider('refresh');
	setFeet(inchid, feetid);        
}

function inchChanged(inchid, cmid, feetid) {
    var oldValue = $(cmid).val() + "";
    var newValue = Math.round($(inchid).val() * 2.54) + "";
    if(oldValue == newValue)
        return;
    $(cmid).val(Number(newValue));
    refreshSlider(1);
	setFeet(inchid, feetid);        
}

$(document).ready(function(){
	setFeet('#inchslider', 'feet');        
    $('#cmslider').change(function(){cmchanged('#inchslider', '#cmslider', '#feet');});
    $('#inchslider').change(function(){inchChanged('#inchslider', '#cmslider', '#feet');});
});

function setFeet(inchid, feetid) {
    var feet = Math.round($(inchid).val() / 12);
    var frac = $(inchid).val() - feet * 12;
    if(frac < 0) {
        feet --;
        frac += 12;
    }
    $(feetid).html(feet + " Feet and " + frac + " Inches");
}

counter = 0;
function refreshSlider(inc) {
    counter += inc;
    if(counter<=0) {
        $('#cmslider').slider('refresh');
        return;
    }
    setTimeout("refreshSlider(-1)", 200);
}

function change() {
    var req = {
        value : $('#cmslider').val()
    };
	var json = JSON.stringify(req);
	$.ajax({
		url : serverBase + "/dating/profile/edit-height",
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
<h3>My Height</h3>

<% 
	DatingProfile profile = (DatingProfile)request.getAttribute("profile");
	int centimeter = profile.getHeight();
	int inches = (int)((float)profile.getHeight() / 2.54);
%>

<div data-role="fieldcontain">
	<div style="width:100%;text-align:center;margin-bottom:8px;">Centimeters</div>
 	<input data-theme='d' type="range" name="cm" id="cmslider" value="<%=centimeter %>" min="0" max="220"  />
</div>

<div data-role="fieldcontain">
	<div style="width:100%;text-align:center;margin-bottom:8px;">Inches</div>
 	<input data-theme='d' type="range" name="inch" id="inchslider" value="<%=inches%>" min="0" max="88"  />
</div>

<div data-role="fieldcontain">
	<div id="feet" style="width:100%;text-align:center;margin-bottom:8px;">0 Feet and 0 Inches</div>
</div>

<div class="ui-body ui-body-b" style='margin-top:0px;'>
	<fieldset class="ui-grid-a">
			<div class="ui-block-a"><a href='#' data-role='button' onclick='history.back()' id="cancel" data-theme="a">Cancel</a></div>
			<div class="ui-block-b"><button data-theme="b" onclick='change()'>Save</button></div>
	</fieldset>
</div>

