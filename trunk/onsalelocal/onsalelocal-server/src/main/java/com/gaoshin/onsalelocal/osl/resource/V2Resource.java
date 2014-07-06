package com.gaoshin.onsalelocal.osl.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

import com.gaoshin.onsalelocal.osl.beans.OfferSearchRequest;
import com.gaoshin.onsalelocal.osl.service.ContentService;
import com.gaoshin.onsalelocal.osl.service.OslService;
import com.gaoshin.onsalelocal.osl.service.PushNotificationService;
import com.gaoshin.onsalelocal.osl.service.SearchService;
import com.gaoshin.onsalelocal.osl.service.UserService;
import com.nextshopper.api.DataSource;
import com.nextshopper.api.Offer;
import com.nextshopper.api.OfferDetails;
import com.nextshopper.api.OfferDetailsList;
import com.nextshopper.api.Store;
import com.nextshopper.api.StoreList;
import com.nextshopper.api.UserOfferDetailsList;
import com.nextshopper.osl.entity.FavouriteOffer;
import com.nextshopper.osl.entity.FavouriteStore;
import com.nextshopper.osl.entity.FollowDetailsList;
import com.nextshopper.osl.entity.FollowStatus;
import com.nextshopper.osl.entity.OfferComment;
import com.nextshopper.osl.entity.OfferCommentDetailsList;
import com.nextshopper.osl.entity.StoreComment;
import com.nextshopper.osl.entity.StoreCommentDetailsList;
import com.nextshopper.osl.entity.StoreDetails;
import com.nextshopper.osl.entity.StoreDetailsList;
import com.nextshopper.osl.entity.StoreOfferDetailsList;
import com.nextshopper.osl.entity.Tag;
import com.nextshopper.osl.entity.TagList;
import com.nextshopper.osl.entity.UserDetailsList;
import com.nextshopper.osl.entity.UserFile;
import com.nextshopper.osl.entity.UserList;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.inject.Inject;
import common.geo.Geocode;
import common.geo.Location;
import common.geo.google.GoogleGeoService;
import common.util.StringUtil;
import common.util.reflection.ReflectionUtil;
import common.util.web.BusinessException;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;
import common.util.web.ServiceError;

@Path("/ws/v2")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class V2Resource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(V2Resource.class.getName());

    @Inject private OslService oslService;
    @Inject private SearchService searchService;
    @Inject private ContentService contentService;
    @Inject private UserService userService;
    @Inject private PushNotificationService notificationService;
    
    private GoogleGeoService geoService;

    public V2Resource() {
    	geoService = new GoogleGeoService();
    }
    
	@GET
	@Path("/trend")
	public OfferDetailsList trend(@QueryParam("latitude") @DefaultValue("37.4238736") String latitude, 
			@QueryParam("longitude") @DefaultValue("-122.0985") String longitude, 
			@QueryParam("radius") @DefaultValue("10") String radius,
			@QueryParam("offset") @DefaultValue("0") String offset ,
			@QueryParam("size") @DefaultValue("20") String size 
			) {
		String userId = getRequesterUserId();
		return oslService.trend(userId, getValue(latitude), getValue(longitude), getIntValue(radius), getIntValue(offset), getIntValue(size));
	}
    
	@GET
	@Path("/location-lookup")
	public Location locationLookup(@QueryParam("latitude") float latitude, 
			@QueryParam("longitude") float longitude) throws Exception {
		Location loc = geoService.fromLatLng(latitude+","+longitude);
		
		return loc;
	}
    
	@GET
	@Path("/nearby-stores")
	public StoreDetailsList nearbyStores(@QueryParam("latitude") String latitude, 
			@QueryParam("longitude") String longitude, 
			@QueryParam("radius") @DefaultValue("1") String radius,
			@QueryParam("offset") @DefaultValue("0") String offset ,
			@QueryParam("offer") @DefaultValue("false") boolean hasOffer ,
			@QueryParam("size") @DefaultValue("20") String size 
			) {
		StoreList stores = oslService.nearbyStores(
						getValue(latitude), 
						getValue(longitude), 
						getValue(radius),
						hasOffer);
		StoreDetailsList list = new StoreDetailsList();
		for(Store s : stores.getItems()) {
			StoreDetails details = ReflectionUtil.copy(StoreDetails.class, s);
			list.getItems().add(details);
		}
 		return list;
	}
    
	@GET
	@Path("/tags")
	public TagList tags() {
		TagList tags = oslService.listTags();
		if(tags.getItems().size() == 0) {
			String[] array = {"Grocery",
						"Home & Garden",
						"Health & Beauty",
						"Clothing and Apparel",
						"Sports, Fitness and Exercise",
						"Baby and Toddler",
						"Toys & Games",
						"Shoes and Footwear",
						"Home Improvement",
						"Remodeling and Repair",
						"Consumer Electronics",
			};
			for(String tag : array) {
				Tag t = new Tag();
				t.setName(tag);
				tags.getItems().add(t);
			}
		}
		return tags;
	}
	
	@GET
	@Path("/search")
	public OfferDetailsList search(
			@QueryParam("keywords") String keywords, 
			@QueryParam("latitude") @DefaultValue("37.4238736") String latitude, 
			@QueryParam("longitude") @DefaultValue("-122.0985") String longitude, 
			@QueryParam("radius") @DefaultValue("10") String radius,
			@QueryParam("offset") @DefaultValue("0") String offset ,
			@QueryParam("size") @DefaultValue("20") String size 
			) throws Exception {
		Float lat = null;
		if(latitude != null) {
			lat = Float.parseFloat(latitude);
		}
		Float lng = null;
		if(longitude != null) {
			lng = Float.parseFloat(longitude);
		}
		OfferSearchRequest req = new OfferSearchRequest();
		req.setLat(lat);
		req.setLng(lng);
		req.setRadius(Float.parseFloat(radius));
		req.setKeywords(keywords);
		req.setSize(getIntValue(size));
		req.setOffset(getIntValue(offset));
		List<OfferDetails> items = searchService.search(req).getItems();
		List<String> ids = new ArrayList<String>();
		for(OfferDetails od : items) {
			ids.add(od.getId());
		}
		String userId = getRequesterUserId();
		return oslService.listOfferDetailByIds(userId, ids, lat, lng);
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
		String userId = getRequesterUserId();
		return oslService.getOfferDetails(userId, offerId);
	}
	
	@POST
	@Path("/offer/like/{offerId}")
	public FavouriteOffer like(@PathParam("offerId") final String offerId) {
		final String userId = assertRequesterUserId();
		FavouriteOffer like = oslService.like(userId, offerId);
		new Thread(new Runnable() { public void run() {notificationService.notifyLikeOffer(userId, offerId);}}).start();
		return like;
	}
	
	@POST
	@Path("/offer/unlike/{offerId}")
	public GenericResponse unlike(@PathParam("offerId") String offerId) {
		String userId = assertRequesterUserId();
		oslService.unlike(userId, offerId);
		return new GenericResponse();
	}
	
	@GET
	@Path("/offer/like/users")
	public UserList listLikes(@QueryParam("offerId") String offerId) {
		return oslService.listLikedUserForOffer(offerId);
	}
	
	@POST
	@Path("/offer/comment")
	public OfferComment commentOffer(OfferComment comment) {
		final String userId = assertRequesterUserId();
		comment.setUserId(userId);
		final OfferComment offerComment = oslService.comment(comment);
		new Thread(new Runnable() { public void run() {notificationService.notifyCommentOffer(userId, offerComment);}}).start();
		return offerComment;
	}
	
	@GET
	@Path("/offer/comments")
	public OfferCommentDetailsList listOfferComments(@QueryParam("offerId") String offerId) {
		String userId = getRequesterUserId();
		return oslService.listOfferComments(userId, offerId);
	}
	
	@POST
	@Path("/user/follow/{userId}")
	public GenericResponse follow(@PathParam("userId") final String userId) {
		final String followerId = assertRequesterUserId();
		if(followerId.equals(userId))
			throw new BusinessException(ServiceError.InvalidInput, "user cannot follow himself");
		oslService.follw(userId, followerId);
		new Thread(new Runnable() { public void run() {notificationService.notifyFollowUser(followerId, userId);}}).start();
		return new GenericResponse();
	}
	
	@POST
	@Path("/user/add-friend/{userId}")
	public GenericResponse addFriend(@PathParam("userId") final String userId) {
		final String followerId = assertRequesterUserId();
		if(followerId.equals(userId))
			throw new BusinessException(ServiceError.InvalidInput, "user cannot follow himself");
		oslService.follow(followerId, userId, FollowStatus.Approved);
		return new GenericResponse();
	}
	
	@POST
	@Path("/user/flag/{userId}")
	public GenericResponse flagUser(@PathParam("userId") String userId) {
		String meId = assertRequesterUserId();
		oslService.flagUser(meId, userId);
		return new GenericResponse();
	}
	
	@POST
	@Path("/offer/flag/{offerId}")
	public GenericResponse flagOffer(@PathParam("offerId") String offerId) {
		String meId = assertRequesterUserId();
		oslService.flagOffer(meId, offerId);
		return new GenericResponse();
	}

	@POST
	@Path("/user/unfollow/{userId}")
	public GenericResponse unfollow(@PathParam("userId") String userId) {
		String followerId = assertRequesterUserId();
		oslService.unfollow(userId, followerId);
		return new GenericResponse();
	}

	@GET
	@Path("/user/followers")
	public FollowDetailsList followers(@QueryParam("userId") String userId) {
		if(userId == null)
			userId = getRequesterUserId();
		String currentUserId = getRequesterUserId();
		FollowDetailsList followers = oslService.followers(userId, currentUserId);
		return followers;
	}

	@GET
	@Path("/user/followings")
	public FollowDetailsList followings(@QueryParam("userId") String userId) {
		if(userId == null)
			userId = getRequesterUserId();
		String currentUserId = getRequesterUserId();
		return oslService.followings(userId, currentUserId);
	}

	@GET
	@Path("/offer-of-followings")
	public UserOfferDetailsList followingsDeals(@QueryParam("userId") String userId,
			@QueryParam("offset") @DefaultValue("0") Integer offset ,
			@QueryParam("size") @DefaultValue("20") Integer size 
			) {
		if(userId == null || userId.trim().length()==0)
			userId = assertRequesterUserId();
		return oslService.followingsDeals(userId, offset, size);
	}

	@GET
	@Path("/store/details") 
	public StoreOfferDetailsList getStoreDetails(@QueryParam("storeId") String storeId
    		, @QueryParam("latitude") String latitude
    		, @QueryParam("longitude") String longitude
			, @QueryParam("offset") @DefaultValue("0") Integer offset
			, @QueryParam("size") @DefaultValue("20") Integer size 
			) {
		String userId = getRequesterUserId();
		logger.info("getStoreDetails");
		StoreDetails storeDetails = oslService.getStoreDetails(storeId);
		logger.info("isFollowingStore");
		boolean liked = oslService.isFollowingStore(storeId, userId);
		storeDetails.setFollowing(liked);
		
		logger.info("listStoreOffers");
		OfferDetailsList offerDetailsList = oslService.listStoreOffers(storeId, userId, offset, size);
		StoreOfferDetailsList list = new StoreOfferDetailsList();
		list.setItems(offerDetailsList.getItems());
		list.setStoreDetails(storeDetails);
		
		if(latitude != null && latitude.trim().length()>0 && longitude != null && longitude.trim().length()>0) {
			float lat = getValue(latitude);
			float lng = getValue(longitude);
			for(OfferDetails o : list.getItems()) {
				if(o.getLatitude() != null && o.getLongitude() != null) {
					float distance = Geocode.distance(lat, lng, o.getLatitude(), o.getLongitude());
					o.setDistance(distance);
				}
			}
		}
		
		return list;
	}
	
	@POST
	@Path("/store/follow/{storeId}")
	public FavouriteStore likeAStore(@PathParam("storeId") final String storeId) {
		final String userId = assertRequesterUserId();
		FavouriteStore followStore = oslService.followStore(userId, storeId);
		new Thread(new Runnable() { public void run() {notificationService.notifyFollowStore(userId, storeId);}}).start();
		return followStore;
	}
	
	@POST
	@Path("/store/unfollow/{storeId}")
	public GenericResponse unlikeAStore(@PathParam("storeId") String storeId) {
		String userId = assertRequesterUserId();
		oslService.unfollowStore(userId, storeId);
		return new GenericResponse();
	}
	
	@GET
	@Path("/store/followers")
	public UserDetailsList listStoreLikes(@QueryParam("storeId") String storeId) {
		String userId = getRequesterUserId();
		return oslService.listLikedUserForStore(storeId, userId);
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
	public OfferDetailsList listStoreOffers(@QueryParam("storeId") String storeId
			, @QueryParam("offset") @DefaultValue("0") Integer offset
			, @QueryParam("size") @DefaultValue("20") Integer size 
			) {
    	String userId = getRequesterUserId();
		return oslService.listStoreOffers(storeId, userId, offset, size);
	}
	
    @POST
    @Path("/upload-image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadImage(@FormDataParam("image") InputStream stream) throws IOException, URISyntaxException {
    	String userId = assertRequesterUserId();
    	UserFile userFile = contentService.save(userId, stream);
    	String imgId = userFile.getFileId();
    	String imgUrl = requestInvoker.get().getRequestURL().toString().replaceAll("upload-image", "file") + "/" + imgId;
    	return Response.seeOther(new URI(imgUrl)).build();
    }
	
    @POST
    @Path("/offer/delete/{id}")
    public GenericResponse deleteOffer(@PathParam("id") String offerId) throws Exception{
    	String userId = assertRequesterUserId();
    	oslService.deleteOffer(offerId);
    	return new GenericResponse();
    }
	
    @POST
    @Path("/offer")
    public Offer submitOffer(final Offer offer) throws Exception{
    	final String userId = assertRequesterUserId();
    	String imgUrl = offer.getLargeImg();
    	UserFile userFile = null;
    	if(imgUrl != null) {
    		String pattern = "/ws/v2/file";
    		int pos = imgUrl.indexOf(pattern);
    		if(pos == -1) {
	    		URL url = new URL(imgUrl);
	    		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    		InputStream inputStream = connection.getInputStream();
		    	userFile = contentService.save(userId, inputStream);
		    	String imgId = userFile.getFileId();
		    	imgUrl = requestInvoker.get().getRequestURL().toString().replaceAll("offer", "file") + "/" + imgId;
		    	inputStream.close();
		    	connection.disconnect();
    		}
    		else {
        		String fileId = imgUrl.substring(pos + pattern.length() + 1);
				userFile = contentService.getByFileId(fileId);
    		}
    	}
    	
    	offer.setSource(DataSource.User.name());
    	offer.setSourceId(userId);
    	
    	if(offer.getStart() == 0)
    		offer.setStart(System.currentTimeMillis());
    	if(offer.getEnd() == 0)
    		offer.setEnd(System.currentTimeMillis() + 7l * 24l * 3600000l);
    	
    	if(offer.getStart() == 0) {
    		offer.setStart(System.currentTimeMillis());
    	}
    	if(offer.getEnd() == 0) {
    		offer.setEnd(System.currentTimeMillis() + 7l * 24l * 3600000l);
    	}
    	
    	if(userFile != null) {
    		offer.setWidth(userFile.getWidth());
    		offer.setHeight(userFile.getHeight());
    	}
    	
    	if((offer.getLatitude() == null || offer.getLongitude() == null)
    			&& (offer.getCity()!=null))
    			{
    		Location geo = geoService.geo(offer.getAddress() + ", " + offer.getCity() + ", " + offer.getState());
    		offer.setLatitude(geo.getGeocode().getLatitude());
    		offer.setLongitude(geo.getGeocode().getLongitude());
    	}
    	oslService.submitOffer(offer);
    	
    	new Thread(new Runnable() { public void run() {notificationService.notifyShareOffer(userId, offer);}}).start();
    	return offer;
    }
	
	@POST
    @Path("/offer")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Offer submitOffer(@FormDataParam("image") InputStream stream
    		, @FormDataParam("title") String title
    		, @FormDataParam("price") String price
    		, @FormDataParam("description") String description
    		, @FormDataParam("merchant") String merchant
    		, @FormDataParam("merchantId") String merchantId
    		, @FormDataParam("address") String address
    		, @FormDataParam("city") String city
    		, @FormDataParam("state") String state
    		, @FormDataParam("country") String country
    		, @FormDataParam("url") String url
    		, @FormDataParam("phone") String phone
    		, @FormDataParam("start") String start
    		, @FormDataParam("end") String end
    		, @FormDataParam("latitude") String latitude
    		, @FormDataParam("longitude") String longitude
    		, @FormDataParam("category") String category
    		, @FormDataParam("tags") String tags
    		, @FormDataParam("largeImg") String imgUrl
    		, @FormDataParam("applyToAll") String applyToAll
    		) throws Exception{
		if(category == null || category.trim().length() == 0)
			category = "";
		if(tags == null || tags.trim().length()==0)
			tags = category.trim();
		else
			tags = tags + " " + category;
			
    	String userId = assertRequesterUserId();
    	UserFile userFile = null;
    	if(stream != null) {
	    	userFile = contentService.save(userId, stream);
	    	if(userFile != null) {
		    	String imgId = userFile.getFileId();
		    	imgUrl = requestInvoker.get().getRequestURL().toString().replaceAll("offer", "file") + "/" + imgId;
	    	}
    	}
    	
    	Offer offer = new Offer();
    	offer.setTitle(title);
        offer.setUrl(url);
    	offer.setPrice(price);
    	offer.setDescription(description);
    	offer.setAddress(address);
    	offer.setCity(city);
    	offer.setState(state);
    	offer.setCountry(country);
    	offer.setPhone(phone);
    	offer.setLargeImg(imgUrl);
    	offer.setSmallImg(imgUrl);
    	offer.setLatitude(getValue(latitude));
    	offer.setLongitude(getValue(longitude));
    	offer.setCreated(System.currentTimeMillis());
    	offer.setTags(StringUtil.nullIfEmpty(tags));
    	offer.setMerchant(StringUtil.nullIfEmpty(merchant));
    	offer.setMerchantId(StringUtil.nullIfEmpty(merchantId));
    	
    	submitOffer(offer);
    	
    	if("true".equalsIgnoreCase(applyToAll)) {
    		oslService.copyOffer(offer);
    	}
    	
        return offer;
    }
	
	private String list2str(List<String> list) {
		if(list == null || list.size() == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(String s : list) {
			if(sb.length() > 0)
				sb.append(";");
			sb.append(s);
		}
		return sb.toString();
	}
    
    @GET
    @Path("/offer/img/{imgId}")
    public Response getAvatar(@PathParam("imgId") String imgId) throws IOException {
    	OutputStream outputStream = responseInvoker.get().getOutputStream();
    	contentService.read(imgId, outputStream);
    	return Response.ok().build();
    }
    
    @GET
    @Path("/file/{id}")
    public Response getFile(@PathParam("id") String id) throws IOException {
    	OutputStream outputStream = responseInvoker.get().getOutputStream();
    	contentService.read(id, outputStream);
    	return Response.ok().build();
    }

    private boolean yipitRunning = false;
    @GET
    @Path("/import-yipit-offer")
    public GenericResponse importYiptiOffer(final @QueryParam("batch") @DefaultValue("10") int batch, final @QueryParam("size") @DefaultValue("10") int size) throws InterruptedException {
    	synchronized (this) {
	        if(yipitRunning)
	        	return new GenericResponse();
	        yipitRunning = true;
        }
    	int total;
        try {
	        List<YipitThread> threads = new ArrayList<V2Resource.YipitThread>();
	        for(int i=0; i<batch; i++) {
	        	YipitThread yi = new YipitThread(size);
	        	threads.add(yi);
	        	yi.start();
	        }
	        
	        total = 0;
	        for(YipitThread yt : threads) {
	        	yt.join();
	        	total += yt.offers;
	        }
        } finally {
        	synchronized (this) {
        		yipitRunning = false;
        	}
        }
    	
    	return new GenericResponse(String.valueOf(total));
    }
    
    private class YipitThread extends Thread {
    	private int offers;
		private int size;

		public YipitThread(int size) {
    		this.size = size;
        }
    	
    	@Override
    	public void run() {
        	this.offers = oslService.importYipitOffer(size);
    	}
    }
}
