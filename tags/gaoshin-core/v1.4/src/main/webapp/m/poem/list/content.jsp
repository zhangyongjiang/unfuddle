<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<script type="text/javascript">
function showForm() {
    if($('#love-form').is(":visible") ){
        $('#love-form').hide();
    } 
    if($('#love-form').is(":hidden") ){
        $('#love-form').show();
    }

}

function uploadPoem() {
    var title = $('#author').val();
    var content = $('#details').val();
    if(content == null || content.trim().length == 0) {
        alert("Poem content is required");
        return false;
    }
    var req = {
        title: title,
        content: content
    };
    var json = JSON.stringify(req);
    
    var url = '<c:url value="/poem/submit"/>';
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

<h3 >Love Poems</h3>

<jsp:include page="/poem/latest?format=object&var=poems"></jsp:include>
<div style="clear:both;">
	<c:if test="${empty poems.list }">
	No pom uploaded so far.
	</c:if>
	
	<c:if test="${not empty poems.list }">
	<ul data-role="listview">
	<c:forEach var="post" items="${poems.list}">
	<li>
		<h3><a href="#" onclick="window.location='<c:url value="/m/poem/index.jsp.oo"/>?id=${post.id }'"><o:first-line str="${post.content }"></o:first-line> </a></h3>
		<p>Uploaded by ${post.author.name } at <o:millisecond-to-date time="${post.createTime.time.time }"></o:millisecond-to-date> </p>
	</li>
	</c:forEach>
	</ul>
	</c:if>
</div>


<div id="love-form" style="display:none;border:solid 2px pink;padding:6px;text-align:center;">
<strong>Upload a Poem</strong>
	<form onsubmit="return uploadPoem();">
	<div data-role="fieldcontain" id="phoneContainer">
	    <label for="author">Author:</label>
	    <input type="text" name="author" id="author" value="" data-theme="d" />
	</div>
	
	<div data-role="fieldcontain">
		<label for="details">Content:</label>
		<textarea style="height:120px;" cols="40" rows="12" name="details" id="details" data-theme="d" ></textarea>
	</div>
	
	<div class="ui-body ui-body-b">
		<fieldset class="ui-grid-a">
				<div class="ui-block-a"><a href='#' data-role='button' id="cancel" data-theme="a" onclick="$('#love-form').hide()">Cancel</a></div>
				<div class="ui-block-b"><button type="submit" data-theme="b">Submit</button></div>
		</fieldset>
	</div>
	</form>
</div>
<div style="clear:both;float:right;margin-top:20px;">
<button data-inline="true" onclick="showForm()">Upload</button>
</div>
