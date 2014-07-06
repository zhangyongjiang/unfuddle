<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="label" required="false" 
%><%@ attribute name="id" required="true" 
%><%@ attribute name="style" required="false" 
%><%@ attribute name="value" required="false" 
%><%@ attribute name="hint" required="false" 
%><tr><td align="right" style="padding:4px;" valign="top" ><div class="form-label">${label}</div></td><td>
<textarea style="<c:if test="${empty style}">width:480px;height:160px;</c:if><c:if test="${not empty style}">${style}</c:if>" name="${id }" id="${id }" >${value}<jsp:doBody/></textarea>
<br/><c:if test="${not empty hint}"><span class="hint">(${hint})</span></c:if>
</td></tr>
