package com.gaoshin.onsalelocal.osl.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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

import com.gaoshin.onsalelocal.api.OfferDetails;
import com.gaoshin.onsalelocal.api.OfferDetailsList;
import com.gaoshin.onsalelocal.osl.entity.Account;
import com.gaoshin.onsalelocal.osl.entity.AccountType;
import com.gaoshin.onsalelocal.osl.entity.FavouriteOfferDetailsList;
import com.gaoshin.onsalelocal.osl.entity.FavouriteStoreDetailsList;
import com.gaoshin.onsalelocal.osl.entity.NotificationDetailList;
import com.gaoshin.onsalelocal.osl.entity.NotificationType;
import com.gaoshin.onsalelocal.osl.entity.OfferComment;
import com.gaoshin.onsalelocal.osl.entity.StoreDetailsList;
import com.gaoshin.onsalelocal.osl.entity.User;
import com.gaoshin.onsalelocal.osl.entity.UserDetails;
import com.gaoshin.onsalelocal.osl.entity.UserFileList;
import com.gaoshin.onsalelocal.osl.entity.UserList;
import com.gaoshin.onsalelocal.osl.logging.MdcKeys;
import com.gaoshin.onsalelocal.osl.logging.RequestId;
import com.gaoshin.onsalelocal.osl.service.ContentService;
import com.gaoshin.onsalelocal.osl.service.OslService;
import com.gaoshin.onsalelocal.osl.service.PushNotificationService;
import com.gaoshin.onsalelocal.osl.service.UserService;
import com.google.android.gcm.server.Message;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.inject.Inject;
import common.geo.Geocode;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/user")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class UserResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(UserResource.class.getName());

    @Inject private UserService userService;
    @Inject private PushNotificationService pushNotificationService;
    @Inject private ContentService contentService;
    @Inject private OslService oslService;
	
    @POST
    @Path("/register")
    public UserDetails register(User user) {
        User newUser = userService.register(user);
        this.setUserIdCookie(newUser.getId().toString());
        return getUserDetails(newUser.getId());
    }
    
    @POST
    @Path("/subscribe/{email}")
    public GenericResponse subscribe(@PathParam("email") String email) {
        String reqid = requestInvoker.get().getHeader(MdcKeys.Reqid.name());
        RequestId ar = new RequestId(reqid);
        userService.subscribe(ar.getUserId(), email);
        return new GenericResponse();
    }
    
    @POST
    @Path("/login")
    public UserDetails login(User user) {
        user = userService.login(user);
        setUserIdCookie(user.getId());
        return getUserDetails(user.getId());
    }
    
    @POST
    @Path("/logout")
    public GenericResponse logout() {
        requestInvoker.get().getSession().invalidate();
        removeUserCookie();
        return new GenericResponse();
    }
	
    @POST
    @Path("/update")
    public UserDetails update(User user) {
        String userId = assertRequesterUserId();
        user.setId(userId);
    	userService.update(user);
        return getUserDetails(userId);
    }
	
    @POST
    @Path("/reset-password/{email}")
    public GenericResponse resetPassword(@PathParam("email") String email) {
    	userService.resetPassword(email);
        return new GenericResponse();
    }
	
    @GET
    @Path("/me")
    public UserDetails me() {
        String userId = getRequesterUserId();
        UserDetails me = getUserDetails(userId);
        me.setPassword(null);
		return me;
    }

    @GET
    @Path("/details")
    public UserDetails getUserDetails(@QueryParam("userId") String userId) {
        UserDetails user = null;
        if(userId != null) {
        	logger.info("getUserDetailsById");
            user = userService.getUserDetailsById(userId);
            String me = getRequesterUserId();
            if(me != null && !me.equals(userId)) {
            	logger.info("check if following");
            	boolean following = oslService.isFollowing(me, userId);
            	user.setMyFollowing(following);
            	
//            	boolean followed = oslService.isFollowing(userId, me);
//            	user.setMyFollower(followed);
            }
        }
        if(user == null) {
            user = new UserDetails();
        }
        return user;
    }

    @POST
    @Path("/facebook-login")
    public UserDetails facebookLogin(Account account) throws Exception {
        String userId = getRequesterUserId();
        account.setUserId(userId);
    	User user = userService.facebookLogin(account);
        setUserIdCookie(user.getId());
		return getUserDetails(user.getId());
    }
    
    @POST
    @Path("/register-android-pn-id/{pnid}")
    public GenericResponse registerAndroidPushNotificationId(@PathParam("pnid") String pnid) {
        String userId = assertRequesterUserId();
        userService.registerPushNotificationId(userId, AccountType.GCM, pnid);
        return new GenericResponse();
    }
    
    @POST
    @Path("/notification/enable")
    public GenericResponse enableNotificationId() {
        String userId = assertRequesterUserId();
        userService.enableNotification(userId, true);
        return new GenericResponse();
    }
    
    @POST
    @Path("/notification/disable")
    public GenericResponse disableNotificationId() {
        String userId = assertRequesterUserId();
        userService.enableNotification(userId, false);
        return new GenericResponse();
    }
    
    @POST
    @Path("/register-ios-pn-id/{pnid}")
    public GenericResponse registerApplePushNotificationId(@PathParam("pnid") String pnid) {
        String userId = assertRequesterUserId();
        userService.registerPushNotificationId(userId, AccountType.APN, pnid);
        return new GenericResponse();
    }
    
    @POST
    @Path("/register-ios-pn-id/{deviceid}/{pnid}")
    public GenericResponse registerApplePushNotificationId(@PathParam("deviceid") String devid,@PathParam("pnid") String pnid) {
        userService.registerPushNotificationId(devid, AccountType.APN, pnid);
        return new GenericResponse();
    }
    
    @POST
    @Path("push-to-android/{regid}")
    public GenericResponse pushToAndroid(@PathParam("regid") String regid, String content) throws IOException {
    	Message.Builder builder = new Message.Builder();
    	builder.addData("message", content);
		Message message = builder.build();
    	pushNotificationService.gcmPush(message, regid);
    	return new GenericResponse();
    }
    
    @GET
    @Path("push-test-msg-to-android")
    public GenericResponse pushToAndroid() throws IOException {
    	Message.Builder builder = new Message.Builder();
    	builder.addData("message", "good");
		Message message = builder.build();
    	String regid = "APA91bE4oJpmNknmCdK84Uwqv6VsCUGYakiGaBjX36957ccLyFkFAdxI-Fhi5VZ9SfxwBQlf1iJM2grzWsvqi2PKW1kdQ7gD2uXAfb0-9YvY6973w4E8kj7qta729d-hfJ8Caz9QI5pn"; 
		pushNotificationService.gcmPush(message, regid);
    	return new GenericResponse();
    }
    
    @GET
    @Path("push-test-msg-to-ios/{token}")
    public GenericResponse pushToIos(@PathParam("token") String token) throws IOException {
    	Map<String, String> extra = new HashMap<String, String>();
    	extra.put("t", String.valueOf(NotificationType.LikeOffer.ordinal()));
    	extra.put("d", String.valueOf(System.currentTimeMillis()));
		pushNotificationService.apnPush("hello world 1", token, extra, 99);
    	return new GenericResponse();
    }
    
    @POST
    @Path("/avatar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public User upload(@FormDataParam("image") InputStream stream) throws Exception{
    	String userId = assertRequesterUserId();
    	contentService.save(userId, stream, userId);
    	User user = userService.getUserById(userId);
    	String requestURI = requestInvoker.get().getRequestURL().toString();
    	user.setImg(requestURI + "/" + userId);
        userService.update(user);
        return user;
    }
    
    @GET
    @Path("/avatar/{userId}")
    public Response getAvatar(@PathParam("userId") String userId) throws IOException {
    	OutputStream outputStream = responseInvoker.get().getOutputStream();
    	contentService.read(userId, outputStream);
    	return Response.ok().build();
    }
    
    @GET
    @Path("/my-fav-offers")
    public FavouriteOfferDetailsList listOffersLikedByMe() {
    	String userId = assertRequesterUserId();
    	return oslService.listOffersLikedByUser(userId, userId);
    }
    
    @GET
    @Path("/user-fav-offers")
    public FavouriteOfferDetailsList listOffersLikedByMe(@QueryParam("userId") String userId) {
    	if(userId == null)
    		userId = assertRequesterUserId();
    	String currentUserId = getRequesterUserId();
    	return oslService.listOffersLikedByUser(userId, currentUserId);
    }
    
    @GET
    @Path("/offers")
    public OfferDetailsList listOffersCreatedByUser(@QueryParam("userId") String userId,
			@QueryParam("offset") @DefaultValue("0") Integer offset ,
			@QueryParam("size") @DefaultValue("20") Integer size 
    		, @QueryParam("latitude") String latitude
    		, @QueryParam("longitude") String longitude
    		) {
    	if(userId == null)
        	userId = assertRequesterUserId();
    	String currentUserId = getRequesterUserId();
    	OfferDetailsList list = oslService.listOffersCreatedByUser(userId, offset, size, currentUserId);
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
    
    @GET
    @Path("/my-offers")
    public OfferDetailsList listOffersCreatedByMe(
			@QueryParam("offset") @DefaultValue("0") Integer offset ,
			@QueryParam("size") @DefaultValue("20") Integer size 
    		, @QueryParam("latitude") String latitude
    		, @QueryParam("longitude") String longitude
    		) {
    	String userId = assertRequesterUserId();
    	return listOffersCreatedByUser(userId, offset, size, latitude, longitude);
    }
    
    @GET
    @Path("/my-fav-stores")
    public FavouriteStoreDetailsList listStoresLikedByMe() {
    	String userId = assertRequesterUserId();
    	String currentUserId = assertRequesterUserId();
    	return oslService.listStoresLikedByUser(userId, currentUserId);
    }
    
    @GET
    @Path("/user-fav-stores")
    public FavouriteStoreDetailsList listStoresLiked(@QueryParam("userId")String userId) {
    	String currentUserId = getRequesterUserId();
    	return oslService.listStoresLikedByUser(userId, currentUserId);
    }
    
    @GET
    @Path("/my-stores")
    public StoreDetailsList listStoresCreatedByMe() {
    	String userId = assertRequesterUserId();
    	String currentUserId = assertRequesterUserId();
    	return oslService.listStoresCreatedByUser(userId, currentUserId);
    }
    
    @GET
    @Path("/files")
    public UserFileList listUserFiles(@QueryParam("userId")String userId) {
    	if(userId == null || userId.trim().length() == 0)
    		userId = assertRequesterUserId();
    	return userService.listFiles(userId);
    }
    
    @POST
    @Path("/notifications/mark-all-read")
    public GenericResponse markAllNotificationsRead() {
    	String userId = assertRequesterUserId();
    	oslService.markAllNotificationsRead(userId);
    	return new GenericResponse();
    }
    
    @GET
    @Path("/notifications")
    public NotificationDetailList listMyNotifications(@QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("size") @DefaultValue("20") int size) {
    	String userId = assertRequesterUserId();
    	return oslService.listUserNotifications(userId, offset, size);
    }
    
    @GET
    @Path("/list")
    public UserList listLatestUser(@QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("size") @DefaultValue("100") int size) {
    	return userService.listLatestUsers(offset, size);
    }
    
    @POST
    @Path("/facebook/post")
    public GenericResponse postToFacebook(OfferComment comment) {
    	String userId = assertRequesterUserId();
    	userService.postToFacebook(userId, comment);
    	return new GenericResponse();
    }
}
