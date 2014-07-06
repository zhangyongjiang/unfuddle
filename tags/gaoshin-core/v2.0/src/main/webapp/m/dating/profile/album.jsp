<%@page import="com.gaoshin.beans.User"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<div data-role="collapsible" data-theme="e" data-collapsed="false" style="padding-right:12px;">
	<h3>Album</h3>
	<jsp:include page="/user/resources/album/${user.id }?format=object&var=albumlist"></jsp:include>
	<c:if test="${empty albumlist.list }">
		<p>No photo uploaded.</p>
	</c:if>
	
	<c:if test="${not empty albumlist.list }">
		<div style="width;100%">
			<c:forEach var="file" items="${albumlist.list}">
				<div style="float:left;text-align:center;width:80px;height:80px;overflow:hidden;padding:6px;">
					<a href='#' onclick='window.location="<c:url value="/m/user/download-album/index.jsp.oo?fid=${file.id}&uid=${file.user.id}"/>"'>
					<img style="width:80px;" src='<c:url value="/user/download-album/${file.id}?width=80"/>'/>
					</a>
				</div>
			</c:forEach>
		</div>
	</c:if>
	
	<c:if test="${me.id == user.id }">
	<div style="clear:both;height:1px;">&nbsp;</div>
	<div style="float:right;" data-inline="true">
		<a href='#' data-theme='c' data-role="button" onclick="Device.pickupPhotoFromGallery(0, 0, '<o:url value="/user/album"/>', null, 'iconUploadSuccessCallback', 'iconUploadErrorCallback')">Upload</a>
	</div>
	</c:if>
</div>
