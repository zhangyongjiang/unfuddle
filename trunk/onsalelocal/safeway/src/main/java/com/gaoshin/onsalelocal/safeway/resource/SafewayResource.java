package com.gaoshin.onsalelocal.safeway.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.safeway.service.SafewayService;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/safeway")
@Component
public class SafewayResource extends JerseyBaseResource {
    @Autowired private SafewayService safewayService;

    @Path("geo")
    @GET
    public GenericResponse geo() {
    	safewayService.geo();
    	return new GenericResponse();
    }

    @Path("seed-tasks")
    @GET
    public GenericResponse seedTasks() {
    	safewayService.seedTasks();
    	return new GenericResponse();
    }
    
}
