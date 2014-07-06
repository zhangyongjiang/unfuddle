<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<%      String postid = request.getParameter("id");
        String url = "/group/post/" + postid + "?format=object&var=post";
%>
<jsp:include page="<%= url %>"></jsp:include>
<%
com.gaoshin.beans.Post post = (com.gaoshin.beans.Post) request.getAttribute("post");
if(post.getParent()!=null) {
    post.getParent().getChildren().add(post);
    request.setAttribute("post", post.getParent());
}
%>

<h3>Feedback - ${post.title }</h3>

<p style="font-color:#999;"> - by <a href="#" onclick="window.location='<c:url value="/m/dating/profile/index.jsp.oo?uid=${post.author.id}"/>'">${post.author.name }</a> at <o:millisecond-to-date time="${post.createTime.time.time }"></o:millisecond-to-date> </p>

${fn:replace(post.content, newLineChar, "<br/>")}

<c:if test="${not empty post.children}">
<div style="margin:32px;">
	<c:forEach var="child" items="${post.children }">
		<h4>Reply: ${child.title }</h4>
		<p style="font-color:#999;"> - by <a href="#" onclick="window.location='<c:url value="/m/dating/profile/index.jsp.oo?uid=${child.author.id}"/>'">${child.author.name }</a> at <o:millisecond-to-date time="${child.createTime.time.time }"></o:millisecond-to-date> </p>
		${fn:replace(child.content, newLineChar, "<br/>")}
	</c:forEach>
</div>
</c:if>

<c:if test="${me.role =='ADMIN' || me.role =='SUPER'}">
<script type="text/javascript" >
function deleteFeedback(postid) {
    if(confirm("Are you sure to delete this feedback?")) {
	    var url = '<c:url value="/group/post/delete"/>/<%=postid%>';
		$.ajax({
			url : url,
			type : "POST",
			contentType:"application/json; charset=utf-8",
			complete : function(transport) {
				if (transport.status == 200) {
				    window.location = '<c:url value="/m/feedback/list/index.jsp.oo"/>';
				} else {
				    alert("Error found. Please try again later.\n" + transport.responseText);
				}
			}
		});
    }
    return false;
}

function showForm() {
    $("#reply-form").show();
    return false;
}

function reply() {
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
    
    var url = '<c:url value="/feedback/reply"/>/<%=postid%>';
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

	
	<button onclick='return deleteFeedback(${post.id})'>Delete</button>
	
	<div id="reply-form" style="border:solid 2px pink;padding:6px;text-align:center;">
	<strong>Reply</strong>
		<form onsubmit="return reply();">
		
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

</c:if>
