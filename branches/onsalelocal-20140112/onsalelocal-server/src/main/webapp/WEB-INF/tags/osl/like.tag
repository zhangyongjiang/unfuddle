<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" 
%><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" 
%><%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" 
%><%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" 
%><%@ attribute name="liked" required="true" type="java.lang.Boolean"  
%><%@ attribute name="cnt" required="true"  
%><%@ attribute name="id" required="true"  
%><div id='likeDiv${id}' style="" onclick='likeOffer("${id}")'>
<% if(liked) {%>
    <img id='likeImg${id}' src='<c:url value="/image_assets/icons30x30/liked.png"/>'/>
<% } else { %>
    <img id='likeImg${id}' src='<c:url value="/image_assets/icons30x30/like.png"/>'/>
<% } %> <span id="likeCnt${id}">${cnt }</span>
</div>
