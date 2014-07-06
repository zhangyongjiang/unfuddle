<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="name" required="true" 
%><%@ attribute name="value" required="true" 
%><%@tag import="common.util.web.JavaScript"
%><input type="hidden" name="<%=name%>" id="<%=name%>" value="<%=JavaScript.escDouble(value)%>"/>