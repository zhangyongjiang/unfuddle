package com.gaoshin.coupon.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;

import com.gaoshin.coupon.bean.StoreList;
import com.gaoshin.coupon.entity.User;
import com.gaoshin.coupon.service.StoreService;
import com.gaoshin.coupon.service.UserService;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/user")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class UserResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(UserResource.class.getName());

    @Inject private UserService userService;
    @Inject private StoreService storeService;
	
    @POST
    @Path("/register")
    public User register(User user) {
        User newUser = userService.register(user);
        this.setUserIdCookie(newUser.getId().toString());
        return newUser;
    }
    
    @POST
    @Path("/mobile-verify")
    public GenericResponse sendMobileVerifyCode(User user) throws Exception {
        user = userService.sendMobileVerifyCode(user);
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

    @GET
    @Path("/my-stores")
    public StoreList listMyStores() {
        String userId = assertRequesterUserId();
        return storeService.listUserStores(userId);
    }
}
