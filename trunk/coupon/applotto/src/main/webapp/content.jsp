<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/appbooster" prefix="a" %>

<a:isUser>
    <jsp:include page="user/content.jsp.oo"></jsp:include>
</a:isUser>
<a:isUser>
    <jsp:include page="user/content.jsp.oo"></jsp:include>
</a:isUser>
