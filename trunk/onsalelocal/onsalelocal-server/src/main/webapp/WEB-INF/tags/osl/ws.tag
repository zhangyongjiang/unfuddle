<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" 
%><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" 
%><%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" 
%><%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" 
%><%@ attribute name="path" required="true"  
%><% path = path.replaceAll("format=object", "format=json"); %><div style="position:absolute;top:0;right:0;z-index:999;">
<% int i=0; for(String s : path.split(" ")) { i++; %>
    <a target="_ws_" href='<c:url value="<%=s%>"/>'><%=i %></a>&nbsp;&nbsp;
<% } %>
</div>
