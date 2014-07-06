package com.gaoshin.onsalelocal.osl.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.osl.beans.Category;
import com.gaoshin.onsalelocal.osl.service.OslCategoryService;
import common.util.StringUtil;

@Path("/ws/category")
@Component
public class CategoryResource extends OslBaseResource {
	@Autowired
	private OslCategoryService categoryService;
	
	@Path("top-categories")
	@GET
	public Category getTopCategories() {
		return categoryService.getTopCategories();
	}

	@Path("nearby")
	@GET
	public Category nearby(@QueryParam("lat") float latitude
			, @QueryParam("lng") float longitude
			, @QueryParam("radius") float radius
			, @QueryParam("category") String category
			, @QueryParam("ls") Boolean localService
			, @QueryParam("merchant") String merchant
			) {
		if(true)
			return getTopCategories();
		merchant = StringUtil.nullIfEmpty(merchant);
		category = StringUtil.nullIfEmpty(category);
		Category cats = categoryService.nearby(latitude, longitude, radius, merchant, localService, category);
		return cats;
	}
}
