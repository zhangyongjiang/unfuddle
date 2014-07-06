<%@page import="java.util.ArrayList"%>
<%@page import="com.gaoshin.beans.Dimension"%>
<%@page import="com.gaoshin.beans.Category"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<%
	ArrayList<Category> chain = new ArrayList<Category>();
	Category cat = (Category)request.getAttribute("it");
	while(cat != null && cat.getId()!=1){
	    chain.add(0, cat);
	    cat = cat.getParent();
	}
%>

<%
	for(Category c : chain) {
%>
<div><img src="/m/images/col-move-top.gif"/> <a href="/shopping/category/<%=c.getId() %>"><%= c.getName() %></a> </div>
<%	    
	}
%>

<c:if test="${not empty it.children }">
<div style="margin-left:32px;">
<c:forEach var="subcat" items="${it.children}">
	<div><img src="/m/images/page-next.gif"/> <a href="/shopping/category/${subcat.id}">${subcat.name }</a></div>
</c:forEach>
</div>
</c:if>

<a href="/m/shopping/create/have/index.jsp.oo?catid=${it.id}">Offer</a>