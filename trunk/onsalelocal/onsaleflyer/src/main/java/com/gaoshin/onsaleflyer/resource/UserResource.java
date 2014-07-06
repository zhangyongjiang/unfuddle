package com.gaoshin.onsaleflyer.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsaleflyer.beans.PasswordChangeRequest;
import com.gaoshin.onsaleflyer.entity.User;
import com.gaoshin.onsaleflyer.service.UserService;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/user")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
@Component
public class UserResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(UserResource.class.getName());

    @Autowired private UserService userService;
	
    @POST
    @Path("/register")
    public User register(User user) {
        User newUser = userService.register(user);
        this.setUserIdCookie(newUser.getId().toString());
        return newUser;
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
	
    @Path("/update")
    @POST
    public GenericResponse update(User user) {
        String userId = assertRequesterUserId();
        user.setId(userId);
    	userService.update(user);
        return new GenericResponse();
    }
	
    @Path("/change-password")
    @POST
    public GenericResponse changePassword(PasswordChangeRequest req) {
        String userId = assertRequesterUserId();
    	userService.changePassword(userId, req);
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
