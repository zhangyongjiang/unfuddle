package com.gaoshin.onsalelocal.walmart.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.walmart.service.WalmartService;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/walmart")
@Component
public class WalmartResource extends JerseyBaseResource {
    @Autowired private WalmartService walmartService;

    @Path("geo")
    @GET
    public GenericResponse geo() {
    	walmartService.geo();
    	return new GenericResponse();
    }

    @Path("seed-tasks")
    @GET
    public GenericResponse seedTasks() {
    	walmartService.seedTasks();
    	return new GenericResponse();
    }
    
}
