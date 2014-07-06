<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="label" required="false" 
%><%@ attribute name="size" required="false" 
%><%@ attribute name="hint" required="false" 
%><tr><td style="padding:4px;" align="right"><div class="form-label">${label}</div></td><td>
<jsp:doBody/>
</td></tr>