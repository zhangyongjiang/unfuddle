<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o"%>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl"%>

<div style="margin-left: -15px;">

	<ul class="nav nav-list">

		<c:if test="${empty me.id }">
            <li class="nav-header"><a target="_self"
                href='<c:url value="/user/login/index.jsp.oo"/>'>Sign in</a></li>
            <li class="nav-header"><a target="_self"
                href='<c:url value="/user/register/index.jsp.oo"/>'>Sign up</a></li>
		</c:if>

		<c:if test="${not empty me.id }">
			<li class="nav-header"><a target="_self"
				href='<c:url value="/offer/upload-image/index.jsp.oo"/>'>Upload
					File</a></li>
			<li class="nav-header"><a target="_self"
				href='<c:url value="/user/files/index.jsp.oo"/>'>My Files</a></li>
			<li class="nav-header"><a target="_self"
				href='<c:url value="/offer/create/index.jsp.oo"/>'>Create Offer</a>
			</li>
            <li class="nav-header"><a target="_self"
                href='<c:url value="/offer/by-user/index.jsp.oo"/>'>My Offers</a></li>
            <li class="nav-header"><a target="_self"
                href='<c:url value="/offer/trend/index.jsp.oo"/>'>Trend</a></li>
			<li class="nav-header"><a target="_self"
				href='javascript:void(0)' onclick="logout()">Logout</a></li>
		</c:if>
	</ul>
</div>