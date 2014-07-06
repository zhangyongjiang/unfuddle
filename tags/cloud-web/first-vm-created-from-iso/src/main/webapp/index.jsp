<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title><jsp:include page="title.jsp.oo"></jsp:include></title>

	<link type="text/css" href='<c:url value="/css/smoothness/jquery-ui-1.8.16.custom.css"/>' rel="stylesheet" />	
	<link rel="stylesheet" type="text/css" href="page.css.oo" />
	<jsp:include page="jquery.jsp.oo"></jsp:include>
	<script type="text/javascript" src='<c:url value="/scripts/jquery-ui-1.8.16.custom.min.js"/>' ></script>
	<script type="text/javascript" src='<c:url value="/scripts/jquery.ba-bbq.min.js"/>' ></script>
	<script type="text/javascript" src='<c:url value="/scripts/jquery.address-1.4.min.js"/>' ></script>
	
	<script type="text/javascript" src='<c:url value="/scripts/json2.js"></c:url>'></script>
	<jsp:include page="page_head.jsp.oo"></jsp:include>
	<script type="text/javascript" >
		base = '<c:url value="/"/>';
		base = base.substring(0, base.length-1);

		$(document).ajaxStart(function(){
        	$("#loading").show();
			//startWait();
		});

		$(document).ajaxStop(function(){
        	$("#loading").hide();
			//endWait();
		});

		$(document).ready(function(){
			$( "#plswait" ).dialog({
				autoOpen: false,
				height: 140,
				modal: true,
				create: function (event, ui) {
			        $(".ui-widget-header").hide();
			    },
			});
		});

		function startWait() {
			$("#plswait").dialog('open');
		}

		function endWait() {
			$("#plswait").dialog('close');
		}
	</script>
</head>
<body>

<div id="header">
<jsp:include page="header.jsp.oo"></jsp:include>
</div>

<div class="sep">&nbsp;</div>

<div id="menu">
<jsp:include page="menu.jsp.oo"></jsp:include>
</div>

<div id="body">
  <table style="border:none;width:100%;"><tr>
	<td style="border:none;padding:0;margin:0;vertical-align: top;width:200px;">
	<div id="navi">
		<jsp:include page="navi.jsp.oo"></jsp:include>
	</div>
	</td>
	<td style="border:none;padding:0;margin:0;vertical-align: top;">
	<div id="content" style="width:96%;">
		<jsp:include page="content.jsp.oo"></jsp:include>
	</div>
	</td>
  </tr></table>
</div>

<div class="sep">&nbsp;</div>

<div id="plswait">
	<center>
	<p>Please wait ...</p>
	<img src='<c:url value="/images/please_wait_25.gif"/>'/> 
	</center>
</div>

</body>
</html>