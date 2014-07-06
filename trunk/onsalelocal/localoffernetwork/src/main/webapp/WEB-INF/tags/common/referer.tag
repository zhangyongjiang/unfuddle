<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="forceRefresh" required="false" 
%><script type="text/javascript">
	window.location = '<%=request.getHeader("referer")%>';
</script>