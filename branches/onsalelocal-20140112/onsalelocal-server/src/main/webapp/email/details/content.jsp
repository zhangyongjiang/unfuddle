<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%
    String param = request.getQueryString();
    String url = "/ws/email/details?format=object&" + (param == null ? "" : param);
    request.getRequestDispatcher(url).include(request, response);
%>

<h3>${it.subject }</h3>
${it.offer }
