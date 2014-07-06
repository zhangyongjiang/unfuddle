<%@tag import="java.util.HashMap"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" 
%><%@ attribute name="items" required="true" type="java.util.List" 
%>
    <table style="border:solid 1px;width:96%;">
        <c:forEach var="deal" items="${items}">
        <tr>
            <td style="vertical-align:top;width:12px;padding:3px;border-top:solid 1px #ccc;"><img width="100" src='${deal.smallImg}'/></td>
            <td style="padding:3px;border-top:solid 1px #ccc;vertical-align:top;">
                <div><a href='<c:url value="/offer/details/index.jsp.oo?offerId="/>${deal.id}'>${deal.title}</a></div>
                        <div style="font-size:1.6em;"><osl:price value="${deal.price}"/> <c:if test="${not empty deal.discount}"><span style='color:red;'>${deal.discount} off</span></c:if></div>
                                <div style="margin-top:10px;"><a href='<o:url-replace key="merchant" value="${deal.merchant}" url="/offer/search/index.jsp.oo?lat=${deal.latitude}&lng=${deal.longitude}&radius=20"/>'>${deal.merchant}</a></div>
                                <div style="font-size:0.8em;color:#666;margin-top:10px;">${deal.address}</div>
                                <div style="font-size:0.8em;color:#666;">${deal.city}, ${deal.state}</div>
								<c:if test="${not empty deal.distance}"><div style="font-size:0.8em;color:#666;">${deal.distance} miles</div></c:if>
                                <div style="float:left;margin-top:10px;font-size:0.8em;color:#bbb;"><o:millisecond-to-date time="${deal.updated}"/></div>
                                <c:if test="${not empty deal.rootSource}"><div style="margin-left:16px;float:left;margin-top:10px;font-size:0.8em;color:#bbb;">${deal.rootSource}</div></c:if>
                                <c:if test="${empty deal.rootSource}"><div style="margin-left:16px;float:left;margin-top:10px;font-size:0.8em;color:#bbb;">${deal.source}</div></c:if>
                                <div style="float:left;margin-top:10px;font-size:0.8em;color:#bbb;margin-left:16px;">${deal.category} ${deal.subcategory} ${deal.tags}</div>
                                <c:if test="${not empty me.id}">
                                        <div style="float:right;margin-top:-24px;">
                                        <c:if test="${deal.bookmarked}"><a href="javascript:void(0);" onclick='unbookmark("${deal.id}")'>unbookmark</a></c:if>
                                        <c:if test="${!deal.bookmarked}"><a href="javascript:void(0);" onclick='bookmark("${deal.id}")'>bookmark</a></c:if>
                                        </div>
                                </c:if>
            </td></tr>
        </c:forEach>
    </table>

