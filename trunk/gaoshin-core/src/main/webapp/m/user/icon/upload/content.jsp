<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<script type="text/javascript" src='<c:url value="/jquery/js/jquery.form.js"/>'></script>

<h3>Upload Icon</h3>

<form id="uploadForm" action='<c:url value="/user/icon"/>' method="post">
<input style="width:100%;" type="file" name="file" id="file">

<div class="ui-body ui-body-b" style='margin-top:20px;'>
	<fieldset class="ui-grid-a">
			<div class="ui-block-a"><a href='#' data-role='button' onclick='history.back()' id="cancel" data-theme="a">Cancel</a></div>
			<div class="ui-block-b"><button data-theme="b" type="submit">Upload</button></div>
	</fieldset>
</div>
</form>

<script type="text/javascript">
function checkResult(responseText, statusText, xhr, $form)  { 
    window.location = '<c:url value="/m/dating/profile/index.jsp.oo"/>';
}
    
$(document).ready(function(){
    var options = {
            success: checkResult
    };
    $('#uploadForm').ajaxForm(options); 
});
</script>