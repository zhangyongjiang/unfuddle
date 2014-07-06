<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" 
%><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" 
%><%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" 
%><%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" 
%><%@ attribute name="value" required="true"  
%><%
	if(value==null) return;
	try {
		Float.parseFloat(value);
		out.write("$" + value);
	} catch (Exception e) {
		out.write(value);
	}
%>
