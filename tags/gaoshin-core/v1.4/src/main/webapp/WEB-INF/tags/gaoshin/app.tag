<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="name" required="true" 
%><%@ attribute name="image" required="true" 
%><%@ attribute name="link" required="true" 
%><%@ attribute name="onclick" required="false" 
%><div class="app">
<a <c:if test="">onClick="${onclick}"</c:if> href='${link }'>
	<img class="app-img" src='${image}'/><br/>
	<div class="app-name">${name}</div>
</a>
</div>