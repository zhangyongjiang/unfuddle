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
First of all, thanks a lot for downloading my application.
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

<p>Again, thanks for your support.</p>

<h3>Tips</h3>
<ul>
	<li>Use the Menu button for navigation</li>
	<li>To upload an image as icon, go to <b>Menu</b> -&gt; <b>ME</b></li>
	<li>To upload more photos, go to <b>Menu</b> -&gt; <b>ME</b> and click the "Upload button" in the album section.</li>
	<li>Select Menu -&gt; Misc -&gt; FAQ for frequently asked questions</li>
	<li>Select Menu -&gt; Feedback to report bugs, make suggestions and complain.</li>
	<li>You can upload your favorite poems in the <a href='#' onclick='window.location="<c:url value="/m/poem/list/index.jsp.oo"/>"'>poem list page</a></li>
	<li>You can send message to somebody when you see the image <img src='<c:url value="/m/images/chat_40x40.png"/>' border='0'/>. 
		In the map view, you need first select an user and then click the button on the top right corner.</li>
</ul>

