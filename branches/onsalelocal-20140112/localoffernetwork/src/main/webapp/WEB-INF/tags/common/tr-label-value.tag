<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="label" required="false" 
%><%@ attribute name="id" required="false" 
%><%@ attribute name="style" required="false" 
%><%@ attribute name="tdstyle" required="false" 
%><%@ attribute name="value" required="false" 
%><tr style="${style}"><td style="border:solid 1px #ddd;padding:4px;" valign="top" >${label}</td>
<td style="border:solid 1px #ddd;padding:4px;">&nbsp;<span name="${id }" id="${id }">${value} <jsp:doBody></jsp:doBody></span>
</td></tr>