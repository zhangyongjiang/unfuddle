<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="pod" required="true" type="com.gaoshin.cloud.web.vm.bean.Pod"
%><a href='<c:url value="/data-center/pod/details/index.jsp.oo?podId="/>${pod.id}'>${pod.name }</a>