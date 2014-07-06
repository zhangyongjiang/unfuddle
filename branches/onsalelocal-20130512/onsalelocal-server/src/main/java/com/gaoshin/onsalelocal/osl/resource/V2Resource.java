package com.gaoshin.onsalelocal.osl.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.gaoshin.onsalelocal.api.DataSource;
import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.OfferDetails;
import com.gaoshin.onsalelocal.api.OfferList;
import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.api.StoreList;
import com.gaoshin.onsalelocal.osl.entity.FavouriteOffer;
import com.gaoshin.onsalelocal.osl.entity.FavouriteStore;
import com.gaoshin.onsalelocal.osl.entity.FollowDetailsList;
import com.gaoshin.onsalelocal.osl.entity.OfferComment;
import com.gaoshin.onsalelocal.osl.entity.OfferCommentDetailsList;
import com.gaoshin.onsalelocal.osl.entity.StoreComment;
import com.gaoshin.onsalelocal.osl.entity.StoreCommentDetailsList;
import com.gaoshin.onsalelocal.osl.entity.StoreDetails;
import com.gaoshin.onsalelocal.osl.entity.StoreDetailsList;
import com.gaoshin.onsalelocal.osl.entity.Tag;
import com.gaoshin.onsalelocal.osl.entity.TagList;
import com.gaoshin.onsalelocal.osl.entity.UserList;
import com.gaoshin.onsalelocal.osl.service.ContentService;
import com.gaoshin.onsalelocal.osl.service.OslService;
import com.gaoshin.onsalelocal.osl.service.UserService;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.inject.Inject;
import common.util.reflection.ReflectionUtil;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/v2")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class V2Resource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(V2Resource.class.getName());

    @Inject private OslService oslService;
    @Inject private ContentService contentService;
    @Inject private UserService userService;
    
	@GET
	@Path("/trend")
	public OfferList trend(@QueryParam("latitude") String latitude, 
			@QueryParam("longitude") String longitude, 
			@QueryParam("radius") @DefaultValue("10") String radius,
			@QueryParam("offset") @DefaultValue("0") String offset ,
			@QueryParam("size") @DefaultValue("20") String size 
			) {
		String userId = getRequesterUserId();
		return oslService.trend(userId, getValue(latitude), getValue(longitude), getIntValue(radius), getIntValue(offset), getIntValue(size));
	}
    
	@GET
	@Path("/following-items")
	public OfferList following() {
		return trend("37.38955", "-122.08432", "20", "0", "20");
	}
    
	@GET
	@Path("/nearby-stores")
	public StoreDetailsList nearbyStores(@QueryParam("latitude") String latitude, 
			@QueryParam("longitude") String longitude, 
			@QueryParam("radius") @DefaultValue("10") String radius,
			@QueryParam("offset") @DefaultValue("0") String offset ,
			@QueryParam("size") @DefaultValue("20") String size 
			) {
		StoreList stores = oslService.nearbyStores(
						getValue(latitude), 
						getValue(longitude), 
						getValue(radius), 
						null, 
						null,
						false);
		StoreDetailsList list = new StoreDetailsList();
		for(Store s : stores.getItems()) {
			StoreDetails details = ReflectionUtil.copy(StoreDetails.class, s);
			details.setOffers(new Random().nextInt(100));
			list.getItems().add(details);
		}
 		return list;
	}
    
	@GET
	@Path("/following-stores")
	public StoreDetailsList followingStores() {
		return nearbyStores("37.38955", "-122.08432", "20", "0", "20");
	}
    
	@GET
	@Path("/tags")
	public TagList tags() {
		TagList list = new TagList();
		for(String s : "Kids Shoes Electronics Woman Man".split(" ")) {
			Tag t = new Tag();
			t.setName(s);
			list.getItems().add(t);
		}
		
		return list;
	}
	
	@GET
	@Path("/search")
	public OfferList search(
			@QueryParam("keywords") String keywords, 
			@QueryParam("latitude") String latitude, 
			@QueryParam("longitude") String longitude, 
			@QueryParam("radius") @DefaultValue("10") String radius,
			@QueryParam("offset") @DefaultValue("0") String offset ,
			@QueryParam("size") @DefaultValue("20") String size 
			) {
		return trend(latitude, longitude, radius, offset, size);
	}
 
	@GET
	@Path("/reset-password")
	public GenericResponse resetPassword(@QueryParam("email") String email) {
		UserResource resource = getResource(UserResource.class);
		resource.resetPassword(email);
		return new GenericResponse();
	}
	
	@GET
	@Path("/offer/details") 
	public OfferDetails getOfferDetails(@QueryParam("offerId") String offerId) {
		return oslService.getOfferDetails(offerId);
	}
	
	@POST
	@Path("/offer/like/{offerId}")
	public FavouriteOffer like(@PathParam("offerId") String offerId) {
		String userId = assertRequesterUserId();
		return oslService.like(userId, offerId);
	}
	
	@GET
	@Path("/offer/like/users")
	public UserList listLikes(@QueryParam("offerId") String offerId) {
		return oslService.listLikedUserForOffer(offerId);
	}
	
	@POST
	@Path("/offer/comment")
	public OfferComment commentOffer(OfferComment comment) {
		String userId = assertRequesterUserId();
		comment.setUserId(userId);
		return oslService.comment(comment);
	}
	
	@GET
	@Path("/offer/comments")
	public OfferCommentDetailsList listOfferComments(@QueryParam("offerId") String offerId) {
		return oslService.listOfferComments(offerId);
	}
	
	@POST
	@Path("/user/follow/{userId}")
	public GenericResponse follow(@PathParam("userId") String userId) {
		String followerId = assertRequesterUserId();
		oslService.follw(userId, followerId);
		return new GenericResponse();
	}

	@GET
	@Path("/user/followers")
	public FollowDetailsList followers(@QueryParam("userId") String userId) {
		if(userId == null)
			userId = getRequesterUserId();
		return oslService.followers(userId);
	}

	@GET
	@Path("/user/followings")
	public FollowDetailsList followings(@QueryParam("userId") String userId) {
		if(userId == null)
			userId = getRequesterUserId();
		return oslService.followings(userId);
	}

	@GET
	@Path("/store/details") 
	public StoreDetails getStoreDetails(@QueryParam("storeId") String storeId) {
		return oslService.getStoreDetails(storeId);
	}
	
	@POST
	@Path("/store/like/{storeId}")
	public FavouriteStore likeAStore(@PathParam("storeId") String storeId) {
		String userId = assertRequesterUserId();
		return oslService.likeStore(userId, storeId);
	}
	
	@GET
	@Path("/store/like/users")
	public UserList listStoreLikes(@QueryParam("storeId") String storeId) {
		return oslService.listLikedUserForStore(storeId);
	}
	
	@POST
	@Path("/store/comment")
	public StoreComment commentStore(StoreComment comment) {
		String userId = assertRequesterUserId();
		comment.setUserId(userId);
		return oslService.commentStore(comment);
	}
	
	@GET
	@Path("/store/comments")
	public StoreCommentDetailsList listStoreComments(@QueryParam("storeId") String storeId) {
		return oslService.listStoreComments(storeId);
	}
	
	@GET
	@Path("/store/offers")
	public OfferList listStoreOffers(@QueryParam("storeId") String storeId) {
		return oslService.listStoreOffers(storeId);
	}
	
    @POST
    @Path("/offer")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Offer submitOffer(@FormDataParam("image") InputStream stream
    		, @FormDataParam("title") String title
    		, @FormDataParam("price") String price
    		, @FormDataParam("discount") String discount
    		, @FormDataParam("description") String description
    		, @FormDataParam("merchant") String merchant
    		, @FormDataParam("address") String address
    		, @FormDataParam("city") String city
    		, @FormDataParam("state") String state
    		, @FormDataParam("phone") String phone
    		) throws Exception{
    	String userId = assertRequesterUserId();
    	String imgId = contentService.save(stream);
    	String imgUrl = requestInvoker.get().getRequestURI() + "/img/" + imgId;
    	
    	Offer offer = new Offer();
    	offer.setTitle(title);
    	offer.setPrice(price);
    	offer.setDiscount(discount);
    	offer.setDescription(description);
    	offer.setAddress(address);
    	offer.setCity(city);
    	offer.setState(state);
    	offer.setPhone(phone);
    	offer.setLargeImg(imgUrl);
    	offer.setSource(DataSource.User.name());
    	offer.setSourceId(userId);
    	oslService.submitOffer(offer);
    	
        return offer;
    }
    
    
    @GET
    @Path("/offer/img/{imgId}")
    public Response getAvatar(@PathParam("imgId") String imgId) throws IOException {
    	OutputStream outputStream = responseInvoker.get().getOutputStream();
    	contentService.read(imgId, outputStream);
    	return Response.ok().build();
    }
}
