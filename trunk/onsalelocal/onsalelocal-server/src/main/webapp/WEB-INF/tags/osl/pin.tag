<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" 
%><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" 
%><%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" 
%><%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" 
%><%@ attribute name="offer" required="true" type="com.gaoshin.onsalelocal.api.OfferDetails" 
%><c:if test="${not empty me.id }">
<c:if test="${not offer.liked}">
<div class="masonry-actionbar"> <a class='ipin-like btn btn-mini' data-post_id='${offer.id }' href="javascript:void(0)"><i class=" icon-heart"></i> Like</a> 
</div>
</c:if>
</c:if>