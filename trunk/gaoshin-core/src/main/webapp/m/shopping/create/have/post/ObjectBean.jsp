<%@page import="com.gaoshin.beans.Category"%>
<%@page import="com.gaoshin.beans.ObjectBean"%>
<% 	ObjectBean ob = (ObjectBean)request.getAttribute("it");
	Category cat = ob.getCategories().get(0).getCategory();
	String url = application.getContextPath() + "/shopping/category/" + cat.getId();
%>
<html>
<head>
<META HTTP-EQUIV="Refresh"
	CONTENT="0; URL=<%=url%>">
</head>
<body></body>
</html>
