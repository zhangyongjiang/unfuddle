package com.gaoshin.onsalelocal.osl.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.gaoshin.onsalelocal.osl.entity.Account;
import com.gaoshin.onsalelocal.osl.entity.AccountType;
import com.gaoshin.onsalelocal.osl.entity.User;
import com.gaoshin.onsalelocal.osl.entity.UserDetails;
import com.gaoshin.onsalelocal.osl.logging.MdcKeys;
import com.gaoshin.onsalelocal.osl.logging.RequestId;
import com.gaoshin.onsalelocal.osl.service.ContentService;
import com.gaoshin.onsalelocal.osl.service.PushNotificationService;
import com.gaoshin.onsalelocal.osl.service.UserService;
import com.google.android.gcm.server.Message;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/user")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class UserResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(UserResource.class.getName());

    @Inject private UserService userService;
    @Inject private PushNotificationService pushNotificationService;
    @Inject private ContentService contentService;
	
    @POST
    @Path("/register")
    public User register(User user) {
        User newUser = userService.register(user);
        this.setUserIdCookie(newUser.getId().toString());
        return newUser;
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
    public User login(User user) {
        user = userService.login(user);
        setUserIdCookie(user.getId());
        return user;
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
    public GenericResponse update(User user) {
        String userId = assertRequesterUserId();
        user.setId(userId);
    	userService.update(user);
        return new GenericResponse();
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
        UserDetails user = null;
        if(userId != null) {
            user = userService.getUserDetailsById(userId);
        }
        if(user == null) {
            user = new UserDetails();
        }
        return user;
    }

    @POST
    @Path("/facebook-login")
    public User facebookLogin(Account account) throws Exception {
        String userId = getRequesterUserId();
        account.setUserId(userId);
    	User user = userService.facebookLogin(account);
        setUserIdCookie(user.getId());
		return user;
    }
    
    @POST
    @Path("/register-android-pn-id/{pnid}")
    public GenericResponse registerAndroidPushNotificationId(@PathParam("pnid") String pnid) {
        String userId = assertRequesterUserId();
        userService.registerPushNotificationId(userId, AccountType.GCM, pnid);
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
    @Path("push-test-msg-to-ios")
    public GenericResponse pushToIos() throws IOException {
    	String token = "4df64d68471b9154d3b21cd669e208c9f6eaed97f676462591a2e20e972dec38"; 
		pushNotificationService.apnPush("hello world 1", token, "/Users/kzhang/onsalelocal/onsalelocal/onsalelocal-server/src/main/resources/apn-dev.p12");
    	return new GenericResponse();
    }
    
    @POST
    @Path("/avatar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public User upload(@FormDataParam("image") InputStream stream) throws Exception{
    	String userId = assertRequesterUserId();
    	contentService.save(stream, userId);
    	User user = userService.getUserById(userId);
    	String requestURI = requestInvoker.get().getRequestURI();
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
}
