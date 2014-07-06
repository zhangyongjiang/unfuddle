<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<div style="position:absolute;top:0;left:0;z-index:9999;font-size:12px;"><%=request.getRequestURI() %></div>
<div style="position:absolute;top:0;right:0;z-index:9999;"><a href='<c:url value="/index.jsp.oo"/>'>Home</a></div>
