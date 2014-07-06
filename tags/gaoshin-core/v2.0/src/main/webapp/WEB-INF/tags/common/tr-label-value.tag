<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="label" required="false" 
%><%@ attribute name="id" required="false" 
%><%@ attribute name="style" required="false" 
%><%@ attribute name="tdstyle" required="false" 
%><%@ attribute name="value" required="false" 
%><tr style="${style}"><td style="${tdstyle}" valign="top" align="right"><b>${label}</b></td>
<td style="${tdstyle}"><span name="${id }" id="${id }">${value} <jsp:doBody></jsp:doBody></span>
</td></tr>