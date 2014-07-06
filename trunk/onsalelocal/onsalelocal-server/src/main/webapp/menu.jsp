<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<div class="well" style="max-width: 340px; padding: 8px 0;">
        
<ul class="nav nav-list">
    <li class="nav-header">
        <a href='<c:url value="/offer/upload-image/index.jsp.oo"/>'>Upload File</a>
    </li>
    <li class="nav-header">
        <a href='<c:url value="/user/files/index.jsp.oo"/>'>My Files</a>
    </li>
    <li class="nav-header">
        <a href='<c:url value="/offer/create/index.jsp.oo"/>'>Create Offer</a>
    </li>
    <li class="nav-header">
        <a href='<c:url value="/offer/by-user/index.jsp.oo"/>'>My Offers</a>
    </li>
    <li class="nav-header">
        <a href='<c:url value="/offer/trend/index.jsp.oo"/>'>Trend</a>
    </li>
</ul>

</div>