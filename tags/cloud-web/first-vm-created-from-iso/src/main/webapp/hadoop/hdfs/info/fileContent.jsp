<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<table class="detail-table" style="border:solid 1px;margin-top:10px;">
<tr><th>Type</th><td>FILE</td></tr>
<tr><th>Size</th><td>${it.size }</td></tr>
<tr><th>Replication</th><td>${it.replication }</td></tr>
<tr><th>Block Size</th><td>${it.blockSize }</td></tr>
<tr><th>Modification Time</th><td><o:millisecond-to-date time="${it.modificationTime }"></o:millisecond-to-date></td></tr>
<tr><th>Permission</th><td>${it.permission }</td></tr>
<tr><th>Owner</th><td>${it.owner }</td></tr>
<tr><th>Group</th><td>${it.group }</td></tr>
<tr><th valign="top">Content</th>
<td valign="top">
	<c:if test="${it.size == 0 }">
		EMPTY FILE
	</c:if>
	
	<c:if test="${it.size > 0 }">
		<pre><c:out value="${it.content }"/></pre>
	</c:if>
</td></tr>
</table>
