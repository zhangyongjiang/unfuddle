<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="label" required="false" 
%><%@ attribute name="id" required="true" 
%><%@ attribute name="value" required="false" 
%><%@ attribute name="size" required="false"  
%><%@ attribute name="hint" required="false" 
%><tr><td style="padding:10px;" align="right"><div class="form-label">${label}</div></td><td>
<input type="password" size="<c:if test="${empty size}">36</c:if><c:if test="${not empty size}">${size }</c:if>" name="${id }" id="${id }" value="${value}" />
<c:if test="${not empty hint}"><span class="hint">(${hint})</span></c:if>
</td></tr>