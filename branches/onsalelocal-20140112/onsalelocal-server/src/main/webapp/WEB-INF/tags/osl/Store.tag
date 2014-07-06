<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" 
%><%@ attribute name="user" required="true" type="com.gaoshin.onsalelocal.osl.entity.User" 
%><div style=''>
    <a target="_self" href='<c:url value="/user/profile/index.jsp.oo?userId=${user.id}"/>'>
        ${user.firstName} ${user.lastName}
    </a>
</div>

