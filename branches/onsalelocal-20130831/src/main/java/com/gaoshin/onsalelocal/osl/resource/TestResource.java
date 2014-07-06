package com.gaoshin.onsalelocal.osl.resource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.api.OfferDetails;
import com.gaoshin.onsalelocal.api.OfferDetailsList;
import com.gaoshin.onsalelocal.osl.entity.Bookmark;
import com.gaoshin.onsalelocal.osl.service.OslService;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/osl")
@Component
public class TestResource extends JerseyBaseResource {
    @Autowired private OslService oslService;

    @Path("deals-by-geo")
    @GET
    public OfferDetailsList searchOffer(
    		@QueryParam("lat") float lat, 
    		@QueryParam("lng") float lng, 
    		@QueryParam("radius") int radius,
    		@QueryParam("offset") @DefaultValue("0") int offset,
    		@QueryParam("size") @DefaultValue("200") int size
    		) throws Exception {
    	OfferDetailsList list = oslService.searchOffer(lat, lng, radius, offset, size);
    	
    	String userId = getRequesterUserId();
    	if(userId != null) {
    		BookmarkResource bookmarkResource = getResource(BookmarkResource.class);
    		List<Bookmark> bklist = bookmarkResource.list(null);
    		Set<String> offerids = new HashSet<String>();
    		for(Bookmark bm : bklist) {
    			offerids.add(bm.getOfferId());
    		}
    		for(OfferDetails od : list.getItems()) {
    			if(offerids.contains(od.getId()))
    				od.setLiked(true);
    		}
    	}
    	
		return list;
    }
    
    @Path("harvest/{schema}")
    @GET
    public GenericResponse harvest(@PathParam("schema") String schema) {
    	int howmany = oslService.harvest(schema);
    	return new GenericResponse(String.valueOf(howmany));
    }
}
