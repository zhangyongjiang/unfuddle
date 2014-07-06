<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<script type="text/javascript">
function showForm() {
    if($('#faq-form').is(":visible") ){
        $('#faq-form').hide();
    } 
    if($('#faq-form').is(":hidden") ){
        $('#faq-form').show();
    }

}

function feedback() {
    var title = $('#title').val();
    var content = $('#details').val();
    if(title == null || title.trim().length == 0 || content == null || content.trim().length == 0) {
        alert("FAQ title and content is required");
        return false;
    }
    var req = {
        title: title,
        content: content,
        thumbup: $('#index').val()
    };
    var json = JSON.stringify(req);
    
    var url = '<c:url value="/faq/submit"/>';
	$.ajax({
		url : url,
		type : "POST",
        data:json,
		contentType:"application/json; charset=utf-8",
		complete : function(transport) {
			if (transport.status == 200) {
			    window.location.reload();
			} else {
			    alert("Error found. Please try again later.\n" + transport.responseText);
			}
		}
	});
    return false;
}
</script>

<div style='float:right;margin:-10px 6px 2px;'><img border="0" width='80' src='<c:url value="/m/images/xo_86x72.png"/>'/></div>

<h3 >FAQ</h3>

<jsp:include page="/faq/list?format=object&var=faqs"></jsp:include>
<div style="clear:both;">
	<c:if test="${empty faqs.list }">
	No faq found so far.
	</c:if>
	
	<c:if test="${not empty faqs.list }">
	<ul data-role="listview">
	<c:forEach var="post" items="${faqs.list}">
	<li>
		<h3><a href="#" onclick="window.location='<c:url value="/m/faq/index.jsp.oo"/>?id=${post.id }'">${post.title}</a></h3>
	</li>
	</c:forEach>
	</ul>
	</c:if>
</div>

<c:if test="${me.role =='ADMIN' || me.role =='SUPER'}">
	<div id="faq-form" style="display:none;border:solid 2px pink;padding:6px;text-align:center;">
	<strong>Write a FAQ</strong>
		<form onsubmit="return feedback();">
		<div data-role="fieldcontain" id="phoneContainer">
		    <label for="title">Question:</label>
		    <input type="text" name="title" id="title" value="" data-theme="d" />
		</div>
		
		<div data-role="fieldcontain">
			<label for="details">Answer:</label>
			<textarea style="height:120px;" cols="40" rows="12" name="details" id="details" data-theme="d" ></textarea>
		</div>
		
		<div data-role="fieldcontain">
			<label for="index">Index:</label>
		    <input type="text" name="index" id="index" value="0" data-theme="d" />
		</div>
		
		<div class="ui-body ui-body-b">
			<fieldset class="ui-grid-a">
					<div class="ui-block-a"><a href='#' data-role='button' id="cancel" data-theme="a" onclick="$('#faq-form').hide()">Cancel</a></div>
					<div class="ui-block-b"><button type="submit" data-theme="b">Submit</button></div>
			</fieldset>
		</div>
		</form>
	</div>
	<div style="clear:both;float:right;margin-top:20px;">
	<button data-inline="true" onclick="showForm()">Create</button>
	</div>
</c:if>