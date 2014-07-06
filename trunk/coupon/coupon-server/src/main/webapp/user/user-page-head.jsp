<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<h3>${it.me.firstName } ${it.me.lastName }</h3>

<table style="width:100%">
	<tr>
	<td onclick='bookmark()'>${it.me.bookmarks }<br/>BOOKMARKS</td>
	<td onclick='tip()'>${it.me.tips }<br/>TIPS</td>
	<td onclick='followings()'>${it.me.followings }<br/>FOLLOWINGS</td>
	<td onclick='followers()'>${it.me.followers }<br/>FOLLOWERS</td>
	</tr>
</table>

<script type="text/javascript">
function bookmark() {
	self.location = '<c:url value="/user/bookmarks/main.jsp.oo"/>';
}
function tip() {
	self.location = '<c:url value="/user/tips/main.jsp.oo"/>';
}

function followings() {
	self.location = '<c:url value="/user/followings/main.jsp.oo"/>';
}
function followers() {
	self.location = '<c:url value="/user/followers/main.jsp.oo"/>';
}
</script>