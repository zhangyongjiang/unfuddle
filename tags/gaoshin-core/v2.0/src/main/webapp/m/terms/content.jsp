<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<div style='float:right;margin:-10px 6px 2px;'><img border="0" width='80' src='<c:url value="/m/images/xo_86x72.png"/>'/></div>
<h3>Terms of Service</h3>

<div class="content">

<jsp:include page="/terms/list?format=object&var=terms"></jsp:include>
<c:forEach var="term" items="${terms.list}">
	<h4>
	<c:if test="${me.role =='ADMIN' || me.role =='SUPER'}">
	<a href='#' onclick='deleteTerms(${term.id})'>
	</c:if>
	${term.title}
	<c:if test="${me.role =='ADMIN' || me.role =='SUPER'}">
	</a>
	</c:if>
	</h4>
	<p>${term.content }</p>
</c:forEach>

</div>


<c:if test="${me.role =='ADMIN' || me.role =='SUPER'}">
	<div id="faq-form" style="border:solid 2px pink;padding:6px;text-align:center;">
	<strong>Write One</strong>
		<form onsubmit="return createTerms();">
		<div data-role="fieldcontain" id="phoneContainer">
		    <label for="title">Title:</label>
		    <input type="text" name="title" id="title" value="" data-theme="d" />
		</div>
		
		<div data-role="fieldcontain">
			<label for="details">Details:</label>
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

<script type="text/javascript">
function deleteTerms(id) {
    if(confirm("are you sure? you may need to copy it first")) {
        var url = '<c:url value="/group/post/delete"/>/' + id;
    	$.ajax({
    		url : url,
    		type : "POST",
    		contentType:"application/json; charset=utf-8",
    		complete : function(transport) {
    			if (transport.status == 200) {
    			    window.location.reload();
    			} else {
    			    alert("Error found. Please try again later.\n" + transport.responseText);
    			}
    		}
    	});
    }
    return false;
}

function createTerms() {
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
    
    var url = '<c:url value="/terms/submit"/>';
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

</c:if>
