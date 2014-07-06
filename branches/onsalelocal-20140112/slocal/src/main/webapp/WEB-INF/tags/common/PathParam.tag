<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="pos" required="true" type="java.lang.Integer"
%><%
String servletPath = request.getServletPath();
String[] path = servletPath.split("/");
if(pos < 0) {
	pos = path.length + pos;
}
if(path.length > pos) {
    out.write(path[pos]);
}

%>