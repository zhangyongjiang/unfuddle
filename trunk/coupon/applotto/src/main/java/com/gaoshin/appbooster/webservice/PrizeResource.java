package com.gaoshin.appbooster.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;

import com.gaoshin.appbooster.bean.PrizeList;
import com.gaoshin.appbooster.entity.Prize;
import com.gaoshin.appbooster.entity.User;
import com.gaoshin.appbooster.entity.UserType;
import com.gaoshin.appbooster.service.PrizeService;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.BusinessException;
import common.util.web.JerseyBaseResource;
import common.util.web.ServiceError;

@Path("/ws/prize")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class PrizeResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(PrizeResource.class.getName());

    @Inject private PrizeService prizeService;
	
    @POST
    @Path("/add")
    public Prize addReward(Prize prize) {
        UserResource ur = getResource(UserResource.class);
        User me = ur.me();
        if(me.getId() == null)
            throw new BusinessException(ServiceError.NoGuest);
        if(!UserType.Super.equals(me.getType()))
            throw new BusinessException(ServiceError.NotAuthorized);
        return prizeService.addPrize(prize);
    }
    
    @GET
    @Path("/all")
    public PrizeList listAllPrizes() {
        return prizeService.listAllPrizes();
    }
    
}
