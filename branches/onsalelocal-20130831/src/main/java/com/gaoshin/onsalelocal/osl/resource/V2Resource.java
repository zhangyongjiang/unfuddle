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

import com.gaoshin.onsalelocal.api.DataSource;
import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.OfferDetails;
import com.gaoshin.onsalelocal.api.OfferDetailsList;
import com.gaoshin.onsalelocal.api.OfferList;
import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.api.StoreList;
import com.gaoshin.onsalelocal.osl.beans.OfferSearchRequest;
import com.gaoshin.onsalelocal.osl.entity.Account;
import com.gaoshin.onsalelocal.osl.entity.AccountType;
import com.gaoshin.onsalelocal.osl.entity.FavouriteOffer;
import com.gaoshin.onsalelocal.osl.entity.FavouriteStore;
import com.gaoshin.onsalelocal.osl.entity.FollowDetails;
import com.gaoshin.onsalelocal.osl.entity.FollowDetailsList;
import com.gaoshin.onsalelocal.osl.entity.OfferComment;
import com.gaoshin.onsalelocal.osl.entity.OfferCommentDetailsList;
import com.gaoshin.onsalelocal.osl.entity.StoreComment;
import com.gaoshin.onsalelocal.osl.entity.StoreCommentDetailsList;
import com.gaoshin.onsalelocal.osl.entity.StoreDetails;
import com.gaoshin.onsalelocal.osl.entity.StoreDetailsList;
import com.gaoshin.onsalelocal.osl.entity.Tag;
import com.gaoshin.onsalelocal.osl.entity.TagList;
import com.gaoshin.onsalelocal.osl.entity.UserFile;
import com.gaoshin.onsalelocal.osl.entity.UserList;
import com.gaoshin.onsalelocal.osl.service.ContentService;
import com.gaoshin.onsalelocal.osl.service.OslService;
import com.gaoshin.onsalelocal.osl.service.PushNotificationService;
import com.gaoshin.onsalelocal.osl.service.SearchService;
import com.gaoshin.onsalelocal.osl.service.UserService;
import com.google.android.gcm.server.Message;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.inject.Inject;
import common.geo.Location;
import common.geo.google.GoogleGeoService;
import common.util.StringUtil;
import common.util.reflection.ReflectionUtil;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

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
	public OfferDetailsList trend(@QueryParam("latitude") String latitude, 
			@QueryParam("longitude") String longitude, 
			@QueryParam("radius") @DefaultValue("10") String radius,
			@QueryParam("offset") @DefaultValue("0") String offset ,
			@QueryParam("size") @DefaultValue("20") String size 
			) {
		String userId = getRequesterUserId();
		return oslService.trend(userId, getValue(latitude), getValue(longitude), getIntValue(radius), getIntValue(offset), getIntValue(size));
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
			@QueryParam("latitude") @DefaultValue("37.385426") String latitude, 
			@QueryParam("longitude") @DefaultValue("-122.091393") String longitude, 
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
		OfferDetailsList result = new OfferDetailsList();
		result.setItems(searchService.search(req).getItems());
		
		String userId = getRequesterUserId();
		if(userId != null) {
			List<OfferDetails> decorateOffers = oslService.decorateOffers(result.getItems(), userId);
			result.setItems(decorateOffers);
		}
		
		return result;
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
	public FavouriteOffer like(@PathParam("offerId") String offerId) {
		String userId = assertRequesterUserId();
		return oslService.like(userId, offerId);
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
		String userId = assertRequesterUserId();
		comment.setUserId(userId);
		return oslService.comment(comment);
	}
	
	@GET
	@Path("/offer/comments")
	public OfferCommentDetailsList listOfferComments(@QueryParam("offerId") String offerId) {
		String userId = getRequesterUserId();
		return oslService.listOfferComments(userId, offerId);
	}
	
	@POST
	@Path("/user/follow/{userId}")
	public GenericResponse follow(@PathParam("userId") String userId) {
		String followerId = assertRequesterUserId();
		oslService.follw(userId, followerId);
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
		FollowDetailsList followers = oslService.followers(userId);
		return followers;
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
	
	@POST
	@Path("/store/unlike/{storeId}")
	public GenericResponse unlikeAStore(@PathParam("storeId") String storeId) {
		String userId = assertRequesterUserId();
		oslService.unlikeStore(userId, storeId);
		return new GenericResponse();
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
    public Offer submitOffer(Offer offer) throws Exception{
    	String userId = assertRequesterUserId();
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
    	assertNotNull(offer.getAddress());
    	assertNotNull(offer.getCity());
    	assertNotNull(offer.getState());
    	
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
    	
    	if(offer.getLatitude() == null || offer.getLongitude() == null) {
    		Location geo = geoService.geo(offer.getAddress() + ", " + offer.getCity() + ", " + offer.getState());
    		offer.setLatitude(geo.getGeocode().getLatitude());
    		offer.setLongitude(geo.getGeocode().getLongitude());
    	}
    	oslService.submitOffer(offer);
    	
    	try {
	        sendNotification(offer);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    	
    	return offer;
    }
	
    private void sendNotification(Offer offer) {
    	String userId = assertRequesterUserId();
    	FollowDetailsList followers = oslService.followers(userId);
    	String msg = "New offer " + offer.getTitle();
    	Message.Builder builder = new Message.Builder();
    	builder.addData("message", msg);
		Message message = builder.build();
    	
    	for(FollowDetails follower : followers.getItems()) {
    		String followerId = follower.getFollowerId();
    		Account account = userService.getAccount(followerId, AccountType.APN);
    		if(account != null) {
    	    	try {
	                notificationService.apnPush(msg, account.getExtId());
                } catch (IOException e) {
	                e.printStackTrace();
                }
    		}
    		account = userService.getAccount(followerId, AccountType.GCM);
    		if(account != null) {
    	    	try {
	                notificationService.gcmPush(message, account.getExtId());
                } catch (IOException e) {
	                e.printStackTrace();
                }
    		}
    	}
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
    		, @FormDataParam("merchantId") String merchantId
    		, @FormDataParam("address") String address
    		, @FormDataParam("city") String city
    		, @FormDataParam("state") String state
    		, @FormDataParam("country") String country
    		, @FormDataParam("phone") String phone
    		, @FormDataParam("start") String start
    		, @FormDataParam("end") String end
    		, @FormDataParam("latitude") String latitude
    		, @FormDataParam("longitude") String longitude
    		, @FormDataParam("tags") String tags
    		, @FormDataParam("category") String category
    		, @FormDataParam("subcategory") String subcategory
    		, @FormDataParam("largeImg") String imgUrl
    		) throws Exception{
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
    	offer.setPrice(price);
    	offer.setDiscount(discount);
    	offer.setDescription(description);
    	offer.setAddress(address);
    	offer.setCity(city);
    	offer.setState(state);
    	offer.setCountry(country);
    	offer.setPhone(phone);
    	offer.setLargeImg(imgUrl);
    	offer.setLatitude(getValue(latitude));
    	offer.setLongitude(getValue(longitude));
    	offer.setCreated(System.currentTimeMillis());
    	offer.setTags(StringUtil.nullIfEmpty(tags));
    	offer.setMerchant(StringUtil.nullIfEmpty(merchant));
    	offer.setMerchantId(StringUtil.nullIfEmpty(merchantId));
    	offer.setCategory(StringUtil.nullIfEmpty(category));
    	offer.setSubcategory(StringUtil.nullIfEmpty(subcategory));
    	
    	submitOffer(offer);
        return offer;
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
