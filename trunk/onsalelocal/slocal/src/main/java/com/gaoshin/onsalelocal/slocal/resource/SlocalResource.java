package com.gaoshin.onsalelocal.slocal.resource;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.api.OfferDetailsList;
import com.gaoshin.onsalelocal.slocal.service.SlocalService;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/slocal")
@Component
public class SlocalResource extends JerseyBaseResource {
    @Autowired private SlocalService slocalService;

    @Path("seed-city-tasks")
    @GET
    public GenericResponse seedCityTasks() throws Exception {
    	slocalService.seedCityTasks();
    	return new GenericResponse();
    }

    @Path("deals-by-geo")
    @GET
    public OfferDetailsList searchOffer(
    		@QueryParam("lat") float lat, 
    		@QueryParam("lng") float lng, 
    		@QueryParam("radius") int radius,
    		@QueryParam("offset") @DefaultValue("0") int offset,
    		@QueryParam("size") @DefaultValue("200") int size
    		) throws Exception {
    	return slocalService.searchOffer(lat, lng, radius, offset, size);
    }

    @Path("geo")
    @GET
    public GenericResponse geo() {
    	slocalService.geo();
    	return new GenericResponse();
    }
    
    @Path("ondemand")
    @GET
    public GenericResponse ondemand(@QueryParam("cityId") String cityId) {
    	slocalService.ondemand(cityId);
    	return new GenericResponse();
    }
}
