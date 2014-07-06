<%@page import="java.io.File"%>
<%@page import="java.util.Random"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o"%>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl"%>

<%
	String path = application.getRealPath("/showcase");
	File showcase = new File(path);
	String[] children = showcase.list();
	Random random = new Random();
	while(true) {
		int index = random.nextInt(children.length);
		if(new File(path + "/" + children[index]).isDirectory() && !children[index].startsWith(".")) {
			String show = "showcase/" + children[index] + "/content.jsp.oo";
			request.getRequestDispatcher(show).include(request, response);
			break;
		}
	}
%>

<div style="position:absolute;top:10px;left:10px;">
	<button onclick="window.location='builder/index.jsp.oo'">Build My Flyer</button>
</div>