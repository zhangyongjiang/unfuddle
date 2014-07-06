<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<%
	String param = request.getQueryString();
	String url = "/ws/user/offers?format=object&" + (param == null ? "" : param);;
	request.getRequestDispatcher(url).include(request, response);
%>

<h3>Offers</h3>

<c:if test="${empty it.items }">No offer found</c:if>

<c:forEach var="offer" items="${it.items }">
    <div style='margin-bottom:20px;border:solid 1px #aaa;padding:8px;'>
        <div>${offer.title }&nbsp;&nbsp;&nbsp;
        <a href='javascript:void(0)' onclick="deleteOffer('${offer.id}')">Delete</a>
        </div>
        
        <a target="_self" href='<c:url value="/offer/details/index.jsp.oo?offerId=${offer.id}"/>'>
        <img style="width:150px;" src='${offer.largeImg}'/></a>
    </div>
</c:forEach>

<script type="text/javascript">
function deleteOffer(id) {
	if(!confirm("are you sure?"))
		return;
    var url = serverBase + "/ws/v2/offer/delete/" + id;
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