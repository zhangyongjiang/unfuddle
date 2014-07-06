<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<h3>Upload Image</h3>

<form method="post" enctype="multipart/form-data" action='<c:url value="/ws/v2/upload-image"/>'>
    <div><input type="file" id="image" name="image"/></div>
    <div><input type="submit"/></div>
</form>

