<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="obj" required="true" type="java.lang.Object"
%><a target=code href=\'<%
if(obj != null) {
    out.write("/xref/com/apptera/britedialer/sdk/beans/" + obj.getClass().getSimpleName() + ".html");
}
%>\'><%= obj == null ? "" : obj.getClass().getSimpleName() %></a>