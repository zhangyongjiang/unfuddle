package com.gaoshin.coupon.webservice;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.gaoshin.coupon.bean.CategoryList;
import com.gaoshin.coupon.service.CategoryService;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/category")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class CategoryResource extends JerseyBaseResource {
    @Inject CategoryService categoryService;
    
    @Path("/tops")
    @GET
    public CategoryList listCategory() {
        return categoryService.listTopCategories();
    }
    
    @Path("/import-shoplocal")
    @POST
    public GenericResponse importShopLocalCategories() throws IOException {
        categoryService.importShoplocalCategories();
        return new GenericResponse();
    }
}
