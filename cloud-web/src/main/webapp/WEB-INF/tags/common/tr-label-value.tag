<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="label" required="false" 
%><%@ attribute name="id" required="false" 
%><%@ attribute name="style" required="false" 
%><%@ attribute name="tdstyle" required="false" 
%><%@ attribute name="value" required="false" 
%><tr style="${style}"><td class="label" style="border:solid 1px #ddd;padding:4px;min-width:160px;" valign="top" >${label}</td>
<td style="border:solid 1px #ddd;padding:4px;"><span name="${id }" id="${id }">${value} <jsp:doBody></jsp:doBody>&nbsp;</span>
</td></tr>