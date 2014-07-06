<%@page import="com.gaoshin.beans.ReviewSummary"%>
<%@page import="com.gaoshin.beans.User"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<%	
	String uid = request.getParameter("uid");
	String fid = request.getParameter("fid");
	String url = "/user/resources/" + fid;
	User me = (User) request.getAttribute("me");
	String pageUrl = "/user/profile-by-id?format=object&var=user&id=" + uid;
	String reviewUrl = "/review/album/" + fid + "?var=review&format=object";
%>
<jsp:include page="<%=pageUrl%>"></jsp:include>

<div id="album" style="width:98%;margin-top:8px;overflow:hidden;text-align:center;padding:6px;">
<img style="width:100%;" src='<c:url value="<%=url%>"/>'/>
<jsp:include page="<%=reviewUrl%>"></jsp:include>
<c:if test="${not empty review.id }">
<br/><% ReviewSummary rs = (ReviewSummary) request.getAttribute("review");
for(int i=0; i<rs.getThumbsup(); i++) {
%>
<img valign="middle" src='<c:url value="/m/images/applause_31x25.gif"/>'/>
<% } %>
</c:if>
</div>

<c:if test="${me.id != user.id }">
<c:if test="${empty review.id }">
	<a href='#' data-role='button' onclick="star()">Like it? Give a <img valign="middle" src='<c:url value="/m/images/applause_31x25.gif"/>'/> now</a>
</c:if>
<c:if test="${not empty review.id }">
	<a href='#' data-role='button' onclick="star()">Also like it? Give an applause now</a>
</c:if>
	<a href='#' data-role="button" onclick="window.location='<o:url value="/m/dating/profile/index.jsp.oo"/>'">Upload Mine</a>
</c:if>

<c:if test="${me.id == user.id }">
	<a href='#' data-role="button" onclick="window.location='<o:url value="/m/dating/profile/index.jsp.oo"/>'">Upload More</a>
</c:if>


<script type="text/javascript">
function star(){
    var req = {
        targetId: <%=fid%>
    };
    var json = JSON.stringify(req);
	$.ajax({
		  url:serverBase + "/user/album/star",
		  type:"POST",
		  data: json,
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  complete: function(transport) {
			     if(transport.status == 200) {
			         window.location.reload();
			     } else {
			         alert('Error: ' + transport.responseText);
			     }
			  }
		});
}
</script>
