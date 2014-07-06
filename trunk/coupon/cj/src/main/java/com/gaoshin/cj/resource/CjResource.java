package com.gaoshin.cj.resource;

import java.util.Date;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import com.gaoshin.cj.beans.LinkDetails;
import com.gaoshin.cj.beans.LinkList;
import com.gaoshin.cj.service.CjBackgroundService;
import com.gaoshin.cj.service.CjService;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/cj")
@Component
public class CjResource extends JerseyBaseResource {
    @Inject private CjService cjService;
    @Inject private CjBackgroundService cjBackgroundService;

    @GET
    public String helloWorld() {
        return "Hello World! Now is " + new Date();
    }
    
    @GET
    @Path("link-details")
    public LinkDetails getLinkDetails(@QueryParam("linkId") String linkId) {
        return cjService.getLinkDetails(linkId);
    }
    
    @GET
    @Path("link-list")
    public LinkList getLinkList(@QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("size") @DefaultValue("10") int size) {
        return cjService.getLinkList(offset, size);
    }
    
    @POST
    @Path("refresh-advertiser-links/{id}") 
    public GenericResponse refreshAdvertiserLinks(@PathParam("id") String advertiserId) throws Exception {
        cjBackgroundService.refreshAdvertiserLinks(advertiserId);
        return new GenericResponse();
    }
}
