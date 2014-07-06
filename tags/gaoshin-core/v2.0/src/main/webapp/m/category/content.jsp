<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

${it.name}
<div style="float:right;"><a href="/category/Shopping">Shopping</a></div>

<form name="fchild" method="post" action='<c:url value="/category/add-children/${it.id}"/>'>
	<input type="hidden" name="parent.id" value="${it.id }">
	<textarea name="children" id="children" style="width:300px;height:200px;"></textarea>
	<input type="submit" value="add children"/>
</form>

<c:if test="${not empty it.children }">
Subcategories:
<ul>
<c:forEach var="child" items="${it.children }">
	<div style="float:left;width:160px;"><a href='<c:url value="/category/${child.id }"/>'>${child.name }</a>
	<o:post url="/category/remove/${child.id }" label="delete"/>
	</div>
</c:forEach>
</ul>
</c:if>

<script type="text/javascript">
	function addChild(nodeName) {
		document.getElementById("children").value = document.getElementById("children").value + "\n" + nodeName;
	}
</script>