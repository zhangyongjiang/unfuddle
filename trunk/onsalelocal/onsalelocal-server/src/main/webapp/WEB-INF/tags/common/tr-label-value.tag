<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="label" required="false" 
%><%@ attribute name="id" required="false" 
%><%@ attribute name="style" required="false" 
%><%@ attribute name="tdstyle" required="false" 
%><%@ attribute name="value" required="false" 
%><tr style="${style}"><td style="padding:4px;width:100px;" align="right"><div class="form-label">
<c:if test="${empty label }">
<c:if test="${not empty id }">
<%= id.substring(0, 1).toUpperCase() + id.substring(1) %>
</c:if>
</c:if>
<c:if test="${not empty label }">
${label}
</c:if>
</div></td>
<td style="padding:4px;">&nbsp;<span name="${id }" id="${id }">${value} <jsp:doBody></jsp:doBody></span>
</td></tr>