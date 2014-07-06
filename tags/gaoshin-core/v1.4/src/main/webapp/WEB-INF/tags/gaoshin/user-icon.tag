<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="user" required="true" type="java.lang.Object"
%><c:if test="${empty user.icon}"><c:url value="/m/images/icon_person_48x48.png"/></c:if><c:if test="${not empty user.icon}"><c:url value="/user/icon"/>/${user.id}</c:if>