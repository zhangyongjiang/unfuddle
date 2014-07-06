<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="url" required="true" 
%><script type="text/javascript">
	window.location = '<c:url value="<%=url%>"/>';
</script>