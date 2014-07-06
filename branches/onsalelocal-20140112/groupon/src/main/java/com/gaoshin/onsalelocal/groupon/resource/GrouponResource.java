package com.gaoshin.onsalelocal.groupon.resource;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.groupon.api.DealResponse;
import com.gaoshin.onsalelocal.groupon.api.DealsResponse;
import com.gaoshin.onsalelocal.groupon.api.GrouponApi;
import com.gaoshin.onsalelocal.groupon.service.GrouponService;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.JerseyBaseResource;

@Path("/ws/groupon")
@Component
public class GrouponResource extends JerseyBaseResource {
    @Inject private GrouponService grouponService;

    @GET
    @Path("deals-by-geo")
    public DealsResponse getDeals(@QueryParam("lat") @DefaultValue("37.3802") float lat, @QueryParam("lng") @DefaultValue("-122.073") float lng, @QueryParam("radius") @DefaultValue("30") int radius) throws Exception {
        return GrouponApi.findDeals(lat, lng, radius);
    }

    @GET
    @Path("deal-details")
    public DealResponse getDealDetails(@QueryParam("dealId") String id) throws Exception {
        return GrouponApi.getDealDetails(id);
    }
    
}
