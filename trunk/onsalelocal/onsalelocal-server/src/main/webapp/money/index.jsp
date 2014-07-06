<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>
<% request.getRequestDispatcher("/ws/user/me?format=object&var=me").include(request, response);%>

<!DOCTYPE html>
<html prefix="og: http://ogp.me/ns#" lang="en-US">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Onsalelocal</title>
<link rel="profile" href="http://gmpg.org/xfn/11">
<link rel="shortcut icon" href="http://www.ipin.gaoshin.com/wordpress/wp-content/themes/ipinpro/favicon.ico">
<link href='<c:url value="/money/js/bootstrap/css/bootstrap.css"/>' rel="stylesheet">
<link href='<c:url value="/money/js/bootstrap/css/bootstrap-responsive.css"/>' rel="stylesheet">
<link href='<c:url value="/money/js/font-awesome/css/font-awesome.css"/>' rel="stylesheet">
<link href='<c:url value="/money/css/style.css"/>' rel="stylesheet">

<!--[if lt IE 9]>
		<script src="http://css3-mediaqueries-js.googlecode.com/svn/trunk/css3-mediaqueries.js"></script>
		<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->

<!--[if IE 7]>
	  <link href="http://www.ipin.gaoshin.com/wordpress/wp-content/themes/ipinpro/css/font-awesome-ie7.css" rel="stylesheet">
	<![endif]-->

</head>

<body>

<jsp:include page="header.jsp.oo"></jsp:include>
<jsp:include page="content.jsp.oo"></jsp:include>

<div class="clearfix"></div>
<jsp:include page="scripts.jsp.oo"></jsp:include>
</body>
</html>