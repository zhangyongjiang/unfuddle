<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="dataCenter" required="true" type="com.gaoshin.cloud.web.vm.bean.DataCenter"
%><a href='<c:url value="/data-center/details/index.jsp.oo?dataCenterId="/>${dataCenter.id}'>${dataCenter.name }</a>