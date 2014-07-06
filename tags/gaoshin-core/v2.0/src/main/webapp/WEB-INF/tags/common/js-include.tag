<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="src" required="true" 
%><% if(request.getAttribute(src)==null) {
	request.setAttribute(src,src);
%><script type="text/javascript" src='<c:url value="${src}"></c:url>'></script>
<%}%>