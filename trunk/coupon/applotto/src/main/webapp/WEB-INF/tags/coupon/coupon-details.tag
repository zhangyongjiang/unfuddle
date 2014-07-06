<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="coupon" required="true" type="com.gaoshin.coupon.entity.Coupon"
%><div style="margin-top: 20px;width:400px;padding:20px;background-size:100% 100%;background-image:url('<c:url value="/images/coupon.gif"/>');">
	<div style="float:right;"><img src="${coupon.imageUrl }"/></div>
	<div style=""><strong>${coupon.title }</strong></div>
	<div style="font-size:0.8em;margin-bottom:14px;color:#aaa;">${coupon.storeName}<br/>${coupon.address}, ${coupon.city} </div>
	<c:if test="${not empty coupon.listPrice}"><div style="margin-top:10px;">\$${coupon.listPrice }</div></c:if>
	<div style="float:right;"><a href='${coupon.url }'>.</a></div>
	<div style="margin-top:16px;">${coupon.description}</div>
	<div style="clear:both;height:1px;">&nbsp;</div>
</div>
