package com.gaoshin.appbooster.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;

import com.gaoshin.appbooster.entity.User;
import com.gaoshin.appbooster.service.UserService;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/user")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class UserResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(UserResource.class.getName());

    @Inject private UserService userService;
	
    @POST
    @Path("/register")
    public User register(User user) {
        User newUser = userService.register(user);
        this.setUserIdCookie(newUser.getId().toString());
        return newUser;
    }
    
    @POST
    @Path("/send-validation-code/{phone}")
    public GenericResponse sendValidationCode(@PathParam("phone") String phone) throws Exception {
        String userId = getRequesterUserId();
        userService.sendMobileVerifyCode(userId, phone);
        return new GenericResponse();
    }
    
    @POST
    @Path("/verify/{code}")
    public GenericResponse vefify(@PathParam("code") String code) throws Exception {
        String userId = getRequesterUserId();
        userService.verify(userId, code);
        return new GenericResponse();
    }
    
    @POST
    @Path("/login")
    public GenericResponse login(User user) {
        user = userService.login(user);
        setUserIdCookie(user.getId());
        return new GenericResponse();
    }
    
    @POST
    @Path("/logout")
    public GenericResponse logout() {
        requestInvoker.get().getSession().invalidate();
        removeUserCookie();
        return new GenericResponse();
    }
	
    @GET
    @Path("/me")
    public User me() {
        String userId = getRequesterUserId();
        User user = null;
        if(userId != null) {
            user = userService.getUserById(userId);
        }
        if(user == null) {
            user = new User();
        }
        return user;
    }
}
