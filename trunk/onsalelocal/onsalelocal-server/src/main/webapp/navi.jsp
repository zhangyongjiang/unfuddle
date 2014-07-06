<%@page import="java.util.Properties"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<ul class="nav nav-list" style="background-color:#ccc;">
        <li class="nav-header">
        <a href='<c:url value="/index.jsp.oo"/>'  target="_self">Home</a>
        </li>
</ul>

<c:if test="${not empty me.id }">
    <div style="margin-top:-25px; margin-left:60px;font-size:0.8em;color:#333;">${me.firstName } ${me.lastName } ${me.id }</div>
</c:if>