package com.gaoshin.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import com.gaoshin.beans.Review;
import com.gaoshin.beans.ReviewSummary;
import com.gaoshin.beans.ReviewTarget;
import com.gaoshin.business.ReviewService;
import com.sun.jersey.spi.inject.Inject;

@Path("/review")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json;charset=utf-8" })
public class ReviewResource extends GaoshinResource {
    @Inject
    private ReviewService reviewService;

    @POST
    @Path("create")
    public Review create(Review review) {
        review.setAuthor(assertUser());
        return reviewService.create(review);
    }

    @GET
    @Path("{type}/{id}") 
    public ReviewSummary list(@PathParam("type") ReviewTarget type, @PathParam("id") Long id) {
        return reviewService.list(type, id);
    }

    @GET
    @Path("list")
    public ReviewSummary listByParam(@QueryParam("type") ReviewTarget type, @QueryParam("id") Long id) {
        return reviewService.list(type, id);
    }
}
