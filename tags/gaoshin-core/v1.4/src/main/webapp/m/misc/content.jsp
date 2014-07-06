<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>


<div style='float:right;margin:-10px 6px 2px;'><img border="0" width='80' src='<c:url value="/m/images/xo_86x72.png"/>'/></div>

<h3 >Misc</h3>

<div style="clear:both;">
	<ul data-role="listview">
	<li>
		<h4><a href="#" onclick="window.location='<c:url value="/m/faq/list/index.jsp.oo"/>'">FAQ</a></h4>
	</li>
	<!--li>
		<h4><a href='#' onclick="window.location='<c:url value="/m/download/index.jsp.oo"/>'">Download</a></h4>
	</li-->
	<li>
		<h4><a href="#" onclick="window.location='<c:url value="/m/terms/index.jsp.oo"/>'">Terms of Service</a></h4>
	</li>
	<li>
		<h4><a href="#" onclick="window.location='<c:url value="/m/privacy/index.jsp.oo"/>'">Privacy Policy</a></h4>
	</li>
	<li>
		<h4><a href="#" onclick="window.location='<c:url value="/m/about-us/index.jsp.oo"/>'">About Us</a></h4>
	</li>
	
	<c:if test="${me.role =='ADMIN' || me.role =='SUPER'}">
	<li>
		<h4><a href="#" onclick="window.location='<c:url value="/m/test/index.jsp.oo"/>'">TEST</a></h4>
	</li>
	</c:if>
	
	</ul>
</div>

