<%@tag import="java.util.HashMap"%><%@ taglib prefix="c"
	uri="http://java.sun.com/jsp/jstl/core"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o"%>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl"%><%@ attribute
	name="offer" required="true"
	type="com.gaoshin.onsalelocal.api.OfferDetails"%>
	

<div class="thumb masonry-brick">

	<div class="thumb-holder">

		<div class="featured-thumb-link">
			<div class="masonry-actionbar">
				<a class='btn btn-small <c:if test="${offer.liked}">active</c:if>' data-post_id="${offer.id}" href="#">
				<i class="icon-2x icon-thumbs-up"></i></a>
			</div>
			<a href="deal.html"><img class="featured-thumb" src="${offer.largeImg}"
				alt="sexy clepatra tags[home furniture]" /></a>
		</div>
		<div class="store-logo">
			<a href="#">${offer.merchant }</a>
		</div>
		<div class="post-title"
			data-title="sexy clepatra tags[painting, egypt]">
			<a href="deal.html">${offer.title} </a>
			<div id="thetags">Expire:<o:millisecond-to-date time="${offer.end }"></o:millisecond-to-date></div>
		</div>
		<div class="masonry-meta masonry-meta-comment-likes text-align-center">
			<span><i class="icon-thumbs-up"></i> ${offer.likes}</span> <span><i
				class="icon-comment"></i> ${offer.comments}</span>
		</div>
	</div>

	<div class="masonry-meta">

            <c:if test="${not empty offer.submitter}">
		<div class="masonry-meta-avatar">
			<a href="#/"><img alt="avatar"
				src="${offer.submitter.img }" /></a>
		</div>

		<div class="masonry-meta-comment">
			<div>Shared by</div>
			<span class="masonry-meta-author"><a href="user.html"
				title="Posts by Genkisan" rel="author">${offer.submitter.firstName}</a></span> <span
				class="masonry-meta-comment-date"><o:millisecond-to-date    time="${offer.created }"></o:millisecond-to-date></span>
		</div>
		</c:if>

	</div>

</div>

