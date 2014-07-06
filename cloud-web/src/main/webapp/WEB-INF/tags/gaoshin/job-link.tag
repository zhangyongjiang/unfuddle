<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="job" required="true" type="com.gaoshin.cloud.web.job.bean.Job"
%><a href='<c:url value="/job/details/index.jsp.oo?id="/>${job.id}'>${job.name }</a>