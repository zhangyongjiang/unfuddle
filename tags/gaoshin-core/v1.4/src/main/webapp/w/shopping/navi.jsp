<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<div id="navi">
<input class="navi-item" type="button" value="I Need" onclick="window.location='/shopping/create/need'" />

<input class="navi-item" type="button" value="I Have" onclick="window.location='/shopping/create/have'" />

<input class="navi-item" type="button" value="Search" onclick="window.location='/shopping/search'" />

</div>