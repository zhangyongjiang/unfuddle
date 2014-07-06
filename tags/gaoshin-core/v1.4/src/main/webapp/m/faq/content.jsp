<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<% 	String poemId = request.getParameter("id"); 
	String url = "/group/post/" + poemId + "?format=object&var=post";
%>
<jsp:include page="<%= url %>"></jsp:include>


<h3>FAQ - ${post.title }</h3>

${fn:replace(post.content, newLineChar, "<br/>")}

<c:if test="${me.role =='ADMIN' || me.role =='SUPER'}">
<script type="text/javascript" >
function deleteFaq(postid) {
    if(confirm("Are you sure to delete this faq?")) {
	    var url = '<c:url value="/group/post/delete"/>/<%=poemId%>';
		$.ajax({
			url : url,
			type : "POST",
			contentType:"application/json; charset=utf-8",
			complete : function(transport) {
				if (transport.status == 200) {
				    window.location = '<c:url value="/m/faq/list/index.jsp.oo"/>';
				} else {
				    alert("Error found. Please try again later.\n" + transport.responseText);
				}
			}
		});
    }
    return false;
}
</script>
<button onclick='return deleteFaq(${post.id})'>Delete</button>
</c:if>
