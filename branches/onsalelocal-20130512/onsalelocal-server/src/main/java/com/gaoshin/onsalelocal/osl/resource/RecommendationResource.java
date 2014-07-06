package com.gaoshin.onsalelocal.osl.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.OfferList;
import com.gaoshin.onsalelocal.osl.beans.Category;
import com.gaoshin.onsalelocal.osl.entity.FavoriteCategory;
import com.gaoshin.onsalelocal.osl.service.RecommendationService;
import common.util.web.GenericResponse;

@Path("/ws/recommendation")
@Component
public class RecommendationResource extends OslBaseResource {
    @Autowired private RecommendationService recommendationService;

    @Path("related")
    @GET
    public OfferList relatedOffer(@QueryParam("id") String offerId) {
        String userId = getUserId();
        if(userId != null) {
        	recommendationService.addUserInterest(userId, offerId);
        }
    	List<Offer> recommendations = recommendationService.getRecommendations(userId, offerId);
    	OfferList list = new OfferList(recommendations);
    	return list;
    }

	@Path("favorites")
	@POST
	public GenericResponse favorites(Category favorites) {
		String userId = assertUserId();
		recommendationService.setFavoriteCategories(userId, favorites.getItems());
		return new GenericResponse();
	}
	
	@Path("favorites")
	@GET
	public Category favorites() {
		String userId = assertUserId();
		List<FavoriteCategory> favorites = recommendationService.getFavoriteCategories(userId);
		Category cat = new Category();
		for(FavoriteCategory fc : favorites) {
			Category c = new Category();
			c.setName(fc.getCategoryId());
			c.setId(fc.getCategoryId());
			cat.getItems().add(c);
		}
		return cat;
	}
}
