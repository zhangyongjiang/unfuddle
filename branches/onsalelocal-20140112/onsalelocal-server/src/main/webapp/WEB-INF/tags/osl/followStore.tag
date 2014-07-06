<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" 
%><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" 
%><%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" 
%><%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" 
%><%@ attribute name="following" required="true" type="java.lang.Boolean"  
%><%@ attribute name="cnt" required="true"  
%><%@ attribute name="id" required="true"  
%><div id='followStoreDiv${id}' style="" onclick='followStoreOffer("${id}")'>
<% if(following) {%>
    <img id='followStoreImg${id}' src='<c:url value="/image_assets/icons30x30/followed.png"/>'/>
<% } else { %>
    <img id='followStoreImg${id}' src='<c:url value="/image_assets/icons30x30/follow.png"/>'/>
<% } %> <span id="followStoreCnt${id}">${cnt }</span>
</div>
