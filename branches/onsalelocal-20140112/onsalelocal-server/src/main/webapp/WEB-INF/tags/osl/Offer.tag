<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" 
%><%@ attribute name="offer" required="true" type="com.gaoshin.onsalelocal.api.Offer" 
%><div style=''>
    <a target="_self" href='<c:url value="/store/profile/index.jsp.oo?offerId=${offer.id}"/>'>
        ${offer.title}
    </a>
</div>

