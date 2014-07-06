<%@tag import="java.util.HashMap"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="url" required="true" 
%><%@ attribute name="label" required="true" 
%><form method="post" action='<c:url value="${url }"/>'><input type='submit' value='${label }'/></form>