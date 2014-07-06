<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o"%>

<div class="col-2">

	<h3>Service Category</h3>
	<ul class="submenu">
		<li><a href='<c:url value="/xen/host/add/index.jsp.oo"/>'>Add Host</a></li>
		<li><a href='<c:url value="/xen/host/list/index.jsp.oo"/>'>Host List</a></li>
		<li><a href='<c:url value="/xen/host/add/index.jsp.oo"/>'>Create VM</a></li>
	</ul>

	<h3 style="margin-top: 16px">Hadoop</h3>
	<ul class="submenu">
		<li><a href='<c:url value="/hadoop/hdfs/info/index.jsp.oo?path=/"/>'>Hadoop File Explorer</a></li>
		<li><a href='<c:url value="/job/create/index.jsp.oo"/>'>New Work Flow</a></li>
		<li><a href='<c:url value="/job/list/index.jsp.oo"/>'>Work Flow List</a></li>
		<li><a href='<c:url value="/job/job-conf/create/index.jsp.oo?jobId=0"/>'>Configuration</a></li>
	</ul>

	<h3 style="margin-top: 16px">Admin</h3>
	<ul class="submenu">
		<li><a href='<c:url value=""/>'>Account</a></li>
		<li><a href='<c:url value=""/>'>Settings</a></li>
		<li><a href='<c:url value=""/>'>Support</a></li>
	</ul>

</div>
