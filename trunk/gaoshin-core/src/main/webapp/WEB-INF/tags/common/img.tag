<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="src" required="true" 
%><img border="0" style="vertical-align: middle;" src="<c:url value="<%= \"/resources/\" + src%>"/>"/>