<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<script type="text/javascript">
function showForm() {
    if($('#feedback-form').is(":visible") ){
        $('#feedback-form').hide();
    } 
    if($('#feedback-form').is(":hidden") ){
        $('#feedback-form').show();
    }

}

function feedback() {
    var title = $('#title').val();
    if(title == null || title.trim().length == 0) {
        alert("Summary is a required field");
        return false;
    }
    
    var content = $('#details').val();
    var req = {
        title: title,
        content: content
    };
    var json = JSON.stringify(req);
    
    var url = '<c:url value="/feedback/submit"/>';
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

<div style='float:right;margin-top:-12px;'><img border="0" width='80' src='<c:url value="/m/images/xo_360.png"/>'/></div>

<h3>Feedbacks</h3>

<jsp:include page="/feedback/latest?format=object&var=fbs"></jsp:include>
<div class="content" style="clear:both;">
	<c:if test="${empty fbs.list }">
	No feedback so far.
	</c:if>
	
	<c:if test="${not empty fbs.list }">
	<ul data-role="listview">
	<c:forEach var="post" items="${fbs.list}">
	<li>
		<h3><a href="#" onclick="window.location='<c:url value="/m/feedback/index.jsp.oo"/>?id=${post.id }'">${post.title }</a></h3>
		<p>${post.content}</p>
		<p style="margin-top:10px;font-color:#999;">by ${post.author.name } at <o:millisecond-to-date time="${post.createTime.time.time }"></o:millisecond-to-date> </p>
	</li>
	</c:forEach>
	</ul>
	</c:if>
</div>

<div id="feedback-form" style="display:none;border:solid 2px pink;padding:6px;text-align:center;">
<strong>Feedback Form</strong>
	<form onsubmit="return feedback();">
	
	<div data-role="fieldcontain" id="phoneContainer">
	    <label for="title">Summary:</label>
	    <input type="text" name="title" id="title" value="" data-theme="d" />
	</div>
	
	<div data-role="fieldcontain">
		<label for="details">Details:</label>
		<textarea style="height:120px;" name="details" id="details" data-theme="d" ></textarea>
	</div>
	
	<div class="ui-body ui-body-b">
		<fieldset class="ui-grid-a">
				<div class="ui-block-a"><a href='#' data-role='button' id="cancel" data-theme="a" onclick="$('#feedback-form').hide()">Cancel</a></div>
				<div class="ui-block-b"><button type="submit" data-theme="b">Submit</button></div>
		</fieldset>
	</div>
	</form>
</div>
<button onclick="showForm()">Send Us Yours</button>

