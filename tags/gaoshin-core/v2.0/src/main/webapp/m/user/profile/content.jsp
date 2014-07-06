<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<div style="clear:both;margin:10px 0 10px;border:solid 1px #ccc;height:1px;">&nbsp;</div>

<div>
<div style="float:left;font-size:1.2em;font-weight:bold;">Profile</div>
<div style="float:right;"><button onclick="window.location='<c:url value="/m/user/edit/index.jsp.oo"/>'">Edit</button></div>
</div>

<div style="clear:both;height:1px;margin-bottom:10px;">&nbsp;</div>
Name: ${it.name }

<div style="clear:both;margin:10px 0 10px;border:solid 1px #ccc;height:1px;">&nbsp;</div>

<div style="float:left;font-size:1.2em;font-weight:bold;">My Locations</div>

<jsp:include page="/location/my?view=my"></jsp:include>