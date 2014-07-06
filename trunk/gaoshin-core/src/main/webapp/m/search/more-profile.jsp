<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<p>
You have completed ${profile.completeness}% of your profile.
</p>

<p>
You must complete at least 50% of your profile before you can search others.
</p>

<p>
We believe that a sincere dating user should complete at least 50% of his profile. The more, the better.
</p>

<div style="float:right;">
<button onclick='window.location="<c:url value="/m/dating/profile/index.jsp.oo"/>"' data-inline="true">Edit My Profile</button>
</div>

