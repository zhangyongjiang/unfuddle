<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<%
        String path = request.getParameter("path");
        String pageUrl = "/hadoop/hdfs/info?path=" + path + "&format=object&var=it";
%>
<jsp:include page="<%= pageUrl %>"></jsp:include>

<div class="page-header">Hadoop File Explorer</div>

<o:tree-link link="/hadoop/hdfs/info/index.jsp.oo?path=__PATH__" path="<%=path%>"></o:tree-link>

<c:if test="${it.dir }">
	<jsp:include page="dirContent.jsp"></jsp:include>
</c:if>

<c:if test="${not it.dir }">
	<jsp:include page="fileContent.jsp"></jsp:include>
</c:if>

