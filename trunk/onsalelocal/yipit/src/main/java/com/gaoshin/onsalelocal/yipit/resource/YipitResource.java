package com.gaoshin.onsalelocal.yipit.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.api.OfferDetailsList;
import com.gaoshin.onsalelocal.yipit.service.YipitService;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/yipit")
@Component
public class YipitResource extends JerseyBaseResource {
    @Autowired private YipitService yipitService;

    @Path("seed-divisions")
    @GET
    public GenericResponse seedDivisions() throws Exception {
    	yipitService.seedDivisions();
    	return new GenericResponse();
    }

    @Path("seed-sources")
    @GET
    public GenericResponse seedSources() throws Exception {
    	yipitService.seedSources();
    	return new GenericResponse();
    }

    @Path("seed-division-tasks")
    @GET
    public GenericResponse seedDivisionTasks() throws Exception {
    	yipitService.seedDivisionTasks();
    	return new GenericResponse();
    }

    @Path("seed-city-tasks")
    @GET
    public GenericResponse seedCityTasks() throws Exception {
    	yipitService.seedCityTasks();
    	return new GenericResponse();
    }

    @Path("deals-by-geo")
    @GET
    public OfferDetailsList searchOffer(@QueryParam("lat") float lat, @QueryParam("lng") float lng, @QueryParam("radius") int radius) throws Exception {
    	return yipitService.searchOffer(lat, lng, radius);
    }
}
