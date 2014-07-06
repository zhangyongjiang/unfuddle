<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="obj" required="true" type="java.lang.Object"
%><%
if(obj == null) {
    out.write("null");
}
else {
    out.write(obj.hashCode());
}
%>