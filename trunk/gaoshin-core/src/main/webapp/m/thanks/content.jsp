<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Random"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<% 	String poemId = request.getParameter("id"); 
	String url = "/group/post/" + poemId + "?format=object&var=poem";
%>
<h3>Thank You!</h3>

<p>
Thanks for downloading my application.
</p>

<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	Date start = sdf.parse("2011/03/01");
	Date now = new Date();
	long days = (now.getTime() - start.getTime()) / 24l / 3600l / 1000l;
%>

<p>
This is my first published Android application. It's launched about <%=days%> days ago. 
It's still young and you might find few users online. Hope you 
could find more and more later.
</p>

<p>
If you have any suggestions, please give you valuable feedback at <a href='#' onclick='window.location="<c:url value="/m/feedback/list/index.jsp.oo"/>"'>here</a>.
</p>

