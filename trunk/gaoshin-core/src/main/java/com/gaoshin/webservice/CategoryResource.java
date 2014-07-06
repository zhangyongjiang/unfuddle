package com.gaoshin.webservice;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.gaoshin.beans.Category;
import com.gaoshin.beans.GenericResponse;
import com.gaoshin.business.ObjectService;
import com.sun.jersey.spi.inject.Inject;

@Path("/category")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json;charset=utf-8" })
public class CategoryResource extends GaoshinResource {
	
    @Inject
    private ObjectService objectService;

    @POST
    @Path("create")
    public Category save(Category category) {
        return objectService.createCategory(category);
    }

    @POST
    @Path("add-children/{catId}")
    public Category addChildren(@PathParam("catId") Long catId, HashMap<String, String> params) {
        String children = params.get("children");
        List<String> childNames = split(children, "[\n\r]+");
        return objectService.createChildCategories(catId, childNames);
    }

    @GET
    @Path("{categoryIdOrName}")
    public Category getCategory(@PathParam("categoryIdOrName") String categoryIdOrName) {
        try {
            Long categoryId = Long.parseLong(categoryIdOrName);
            return objectService.getCategory(categoryId);
        } catch (Exception e) {
            return objectService.getCategoryByName(categoryIdOrName);
        }
    }

    @POST
    @Path("remove/{catid}")
    public GenericResponse remove(@PathParam("catid") Long categoryId) {
        objectService.removeCategory(categoryId);
        return new GenericResponse();
    }

    @POST
    @Path("add-dimension/{categoryId}/{dimensionId}")
    public Category addDimension(@PathParam("categoryId") Long categoryId, @PathParam("dimensionId") Long dimensionId) {
        return objectService.addDimension(categoryId, dimensionId);
    }

    @POST
    @Path("remove-dimension/{categoryId}/{dimensionId}")
    public GenericResponse removeDimension(@PathParam("categoryId") Long categoryId,
            @PathParam("dimensionId") Long dimensionId) {
        objectService.removeDimension(categoryId, dimensionId);
        return new GenericResponse();
    }
}
