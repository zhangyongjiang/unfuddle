<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<%
	String param = request.getQueryString();
	String url = "/ws/v2/trend?format=object&" + (param == null ? "latitude=37.4238736&longitude=-122.0985" : param);;
	request.getRequestDispatcher(url).include(request, response);
%>

<h3>Trend</h3>

<c:if test="${empty it.items }">No offer found</c:if>

<c:forEach var="offer" items="${it.items }">
    <div style='margin-bottom:20px;border:solid 1px #aaa;padding:8px;'>
        <div>${offer.title }&nbsp;&nbsp;&nbsp;
        <c:if test="${offer.liked}">
            Liked
        </c:if>
        <c:if test="${not offer.liked}">
            <a id='like${offer.id}' href='javascript:void(0)' onclick="likeOffer('${offer.id}')">Like</a>
        </c:if>
        </div>
        
        <a target="_self" href='<c:url value="/offer/details/index.jsp.oo?offerId=${offer.id}"/>'>
        <img style="width:150px;" src='${offer.largeImg}'/></a>
    </div>
</c:forEach>

<script type="text/javascript">
function likeOffer(id) {
    var url = serverBase + "/ws/v2/offer/like/" + id;
    $.ajax({
          url:url,
          type:"POST",
          contentType:"application/json; charset=utf-8",
          dataType:"json",
          complete: function(transport) {
                 if(transport.status == 200) {
                     window.location.reload();
                 } else {
                     alert('Error: ' + transport.status + ", " + transport.responseText);
                 }
              }
        });

}
</script>