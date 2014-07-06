package com.gaoshin.points.server.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gaoshin.points.server.bean.Cashier;
import com.gaoshin.points.server.bean.User;
import com.gaoshin.points.server.bean.UserBalanceList;
import com.gaoshin.points.server.service.MerchantService;
import com.gaoshin.points.server.service.UserService;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.BusinessException;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;
import common.util.web.ServiceError;

@Path("/user")
@Component
@Scope("request")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class UserResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(UserResource.class.getName());

	@Inject private UserService userService;
	@Inject private MerchantService merchantService;
	
	@POST
	@Path("/signup")
	public User signup(User user) {	 
        logger.debug("signup " + user.getPhone() + " from " + getRemoteIp());
		User newUser = userService.signup(user);		
		return newUser;
	}
	
	@POST
	@Path("/login")
	public User login(User user) {
		User existing = userService.login(user);
		this.setUserIdCookie(existing.getId().toString());
		return existing;
	}
		
	@Path("/cashier-login")
	@POST
	public GenericResponse cashierLogin(Cashier cashier) {	 
		Cashier login = merchantService.cashierLogin(cashier);
		setUserIdCookie(login.getId());
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
    	String userId = assertRequesterUserId();
        return getProfileById(userId);
    }

	@GET
	@Path("/profile-by-id/{userId}")
	public User getProfileById(@PathParam("userId") String userId) {
		User user = userService.getUserById(userId);
		if(user == null)
			throw new BusinessException(ServiceError.NotFound);
		return user;
	}
	
	@GET
	@Path("/balance")
	public UserBalanceList listBalance() {
    	String userId = assertRequesterUserId();
    	return userService.listBalance(userId);
	}
}
