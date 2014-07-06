<%@tag import="java.util.HashMap"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" 
%><%@ attribute name="offer" required="true" type="com.gaoshin.onsalelocal.api.OfferDetails" 
%>
<div style='border-bottom:solid 1px #ccc;padding:8px;margin-bottom:24px;margin-right:16px;width:360px;' class="offerSummary">
    <a target="_self" href='<c:url value="/offer/details/index.jsp.oo?offerId=${offer.id}"/>'>
    <img style="width:360px;" src='${offer.largeImg}'/>
    </a>
    
    <div style="width:360px;">${offer.title }</div>
    <div style="color:#aaa;font-size:0.7em;">Expire: <o:millisecond-to-date time="${offer.end }"></o:millisecond-to-date>  </div>
    <div style="float:right;">
        <osl:like id="${offer.id }" cnt="${offer.likes }" liked="${offer.liked }"></osl:like>
    </div>
    <div><a href='<osl:store-link id="${offer.merchantId }"/>'>${fn:substring(offer.merchant, 0, 32) }</a></div>
    <div style="color:#aaa;font-size:0.7em;">${offer.distance } miles</div>
    
    <c:if test="${not empty offer.submitter.id }">
        <div style="color:#aaa;font-size:0.7em;">Submitted by <a target='_self' href='<c:url value="/user/profile/index.jsp.oo?userId="/>${offer.submitter.id}'>${offer.submitter.firstName }</a></div>
    </c:if>
    
</div>

