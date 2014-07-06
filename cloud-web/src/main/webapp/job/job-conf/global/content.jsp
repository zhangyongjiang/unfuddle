<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/KISS/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/KISS/ymeta" prefix="y" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h1>Job Global Configurations</h1>

<table>
	<c:forEach var="conf" items="${it}">
		<o:tr-label-value tdstyle="text-align:left;border:solid 1px #ccc;padding:6px;" label="${conf.key}" >
			${conf.value} 
			<a href='<c:url value="/job/cron/job-conf/edit/${conf.id}"/>'>edit</a> 
			<a href='<c:url value="/job/cron/job-conf/delete/${conf.id}"/>'><img src='<c:url value="/KISS/images/delete_icon.gif"/>' border="0"/></a>
		</o:tr-label-value>
	</c:forEach>
</table>
