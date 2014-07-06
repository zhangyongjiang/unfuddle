<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="menu-item"><a href=''>Home</a></div>
<div class="menu-item"><a href=''>TOS</a></div>
<div class="menu-item"><a href=''>About</a></div>
<div class="menu-item"><a href=''>Contact</a></div>

<sec:authorize access="hasRole('User')">
	<div class="menu-item"><a href='<c:url value="/j_spring_security_logout"/>'>Logout</a></div>
</sec:authorize>
