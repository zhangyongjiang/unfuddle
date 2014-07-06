<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

In category ${it.name }


<form method="post" action="/shopping/create/have/${it.id }">
	<input type="text" name="title" id="title" style="width:60%;"/><br/>
	<textarea name="description" id="description" style="width:60%;height:300px;"></textarea><br/>
	<input type="submit"/>
</form>

