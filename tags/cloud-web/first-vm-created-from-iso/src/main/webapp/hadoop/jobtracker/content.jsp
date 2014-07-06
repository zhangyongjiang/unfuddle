<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%
	String hadoopJobId = request.getParameter("hadoopJobId");
%>
<div class="page-header">Hadoop Job Tracker</div>

<iframe style="width:880px;height:3600px;border:none;" src="http://gaoshin.com:50030/jobdetails.jsp?jobid=<%=hadoopJobId%>&refresh=0">
</iframe>
