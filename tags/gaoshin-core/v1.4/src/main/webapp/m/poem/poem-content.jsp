<%@page import="java.util.Random"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<div style="margin:0 0 16px;border-top:solid 1px purple;height:1px;width:100%;">&nbsp;</div>

<% Random random = new Random(System.currentTimeMillis()); 
	int index = random.nextInt(100) + 1;
	String contextPath = request.getContextPath();
	String fileName = "/m/poem/images/" + index + ".jpg";
%>
<div style="max-width:640px;width:100%;overflow:hidden;position:absolute;top:0px;left:0px;z-index:0;padding:0;"><img style="width:100%;opacity:0.45;filter:alpha(opacity=45)" border="0" src='<c:url value="<%=fileName%>"/>'/></div>
	<div style="padding:16px;font-family:'courier new';font-weight:bold;">
	${fn:replace(poem.content, newLineChar, "<br/><br/>")}
	
	<c:if test="${not empty poem.title }">
		<div style="clear:both; margin-top:20px;font-size:0.8em;">
		- by ${poem.title }
		</div>
	</c:if>
</div>

<div style="float:right;" data-inline="true">
<a href="#" data-theme="d" data-role='button' onclick="window.location='<c:url value="/m/poem/random/index.jsp.oo"/>'">&gt;&gt;</a>
</div>

<div style="clear:both;margin:36px 0 16px;height:1px;width:100%;">&nbsp;</div>

	<div data-role="navbar">
		<ul>
			<li><a href="#" onclick="window.location='<c:url value="/m/dating/profile/index.jsp.oo?uid=${poem.author.id}"/>'">Poem uploaded by <br/>${poem.author.name }</a></li>
			<li><a href="#" >Images provided <br/>by friends of XO</a></li>
		</ul>
	</div>

<script type="text/javascript">
	var pos = <o:obj2json obj="${poem}"/>;
	function sendSms() {
	    sms('', pos.content);
	}
	function sendEmail() {
	    email('', pos.content);
	}
</script>
<div style="clear:both;">
	<fieldset class="ui-grid-a">
		<div class="ui-block-a"><a href="#" onclick="sendSms()" data-role="button" >Sms</a></div>
		<div class="ui-block-b"><a href="#" onclick="sendEmail()" data-role="button" >Email</a></div>
	</fieldset>
</div>

	
<c:if test="${me.role =='ADMIN' || me.role =='SUPER'}">
<script type="text/javascript" >
function deletePom(postid) {
    var url = '<c:url value="/group/post/delete"/>/${poem.id}';
	$.ajax({
		url : url,
		type : "POST",
		contentType:"application/json; charset=utf-8",
		complete : function(transport) {
			if (transport.status == 200) {
			    window.location = '<c:url value="/m/poem/list/index.jsp.oo"/>';
			} else {
			    alert("Error found. Please try again later.\n" + transport.responseText);
			}
		}
	});
    return false;
}
</script>
<div style="clear:both;"><button onclick='return deletePom(${poem.id})' data-theme="a">Delete</button></div>
</c:if>

