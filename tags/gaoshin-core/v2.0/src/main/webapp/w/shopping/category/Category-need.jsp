<%@page import="com.gaoshin.beans.Dimension"%>
<%@page import="com.gaoshin.beans.Category"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<div style="clear:both;height:1px;">&nbsp;</div>

<% Category cat = (Category)request.getAttribute("it");
while(cat.getParent() != null && !cat.getName().equals("Shopping")){
    Category parent = cat.getParent();
    cat = cat.getParent();
%>
<div style="float:left;width:120px;"><a href="/shopping/create/need/<%=parent.getId() %>"><%= parent.getName() %></a></div>
<%
}
%>

<div>
<h2>${it.name}</h2>
</div>

<c:if test="${not empty it.children }">
Subcategories:<br/>
<c:forEach var="subcat" items="${it.children}">
	<div style="width:120px;float:left;"><a href="/shopping/create/need/${subcat.id}">${subcat.name }</a></div>
</c:forEach>
</c:if>

<% cat = (Category)request.getAttribute("it");
while(cat != null){
    for(Dimension dim : cat.getDimensions()) {
%>
<%=dim.getName() %><br/>
<%
    }
    cat = cat.getParent();
}
%>
