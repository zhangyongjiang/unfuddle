<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="label" required="false" 
%><%@ attribute name="size" required="false" 
%><%@ attribute name="hint" required="false" 
%><tr><td style="padding:10px;" align="right"><div class="form-label">${label}</div></td><td>
<jsp:doBody/>
<span id="${id }ErrorMsg" style="color:red;"></span>
<c:if test="${not empty hint}"><span class="hint">(${hint})</span></c:if>
</td></tr>