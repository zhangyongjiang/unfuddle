<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><jsp:include page="title.jsp.oo"/></title>

<script type="text/javascript" src='<c:url value="/script/json2.js"/>'></script>
<script type="text/javascript" src='<c:url value="/device.jsp"/>'></script>
<script type="text/javascript" src='<c:url value="/jquery/js/jquery-1.5.js"/>'></script>
<script type="text/javascript" src='<c:url value="/jquery/js/jquery.timer-1.2.js"/>'></script>
<script type="text/javascript" src='<c:url value="/jquery/js/jquery.query-2.1.7.js"/>'></script>

<style type="text/css">
a {
	text-decoration:none;
}

#viewarea {
	height: 100%;
	width: 100%;
};
</style>
