<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<jsp:include page="content-header.jsp.oo"/>

<jsp:include page="/m/shopping/category/Category-need.jsp.oo"></jsp:include>

<form method="post" action="/shopping/<jsp:include page="customer-type.jsp.oo"/>">
	<textarea name="title"></textarea>
</form>

<%= request.getParameter("view") %>