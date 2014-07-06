<%@page import="java.util.Random"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<% 	String poemId = request.getParameter("id"); 
	String url = "/group/post/" + poemId + "?format=object&var=poem";
%>
<jsp:include page="<%= url %>"></jsp:include>
<jsp:include page="poem-content.jsp.oo"></jsp:include>
