<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="path" required="true" 
%><%
int port = request.getServerPort();
String contextPath = application.getContextPath();
String url = "http://localhost:" + port + contextPath + path;
%><c:import url="<%=url%>"/><!-- <%=url%> -->