package com.gaoshin.onsalelocal.osl.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.api.Category;
import com.gaoshin.onsalelocal.api.CategoryBean;
import com.gaoshin.onsalelocal.osl.service.OslCategoryService;
import common.util.web.GenericResponse;

@Path("/ws/category")
@Component
public class CategoryResource extends OslBaseResource {
	@Autowired
	private OslCategoryService categoryService;
	
	@Path("top-categories")
	@GET
	public CategoryBean getTopCategories() {
		return categoryService.getTopCategories();
	}
	
	@Path("seed")
	@POST
	public GenericResponse seed() {
		String[] cats = {
				"Food & Grocery",
				"Home & Garden",
				"Beauty & spa",
				"Events & Activities",
				"Baby & Toddler",
				"Sports & fitness",
				"Shoes & Footwear",
				"Clothing & Apparel",
				"Toys & Games",
				"Electronics & computer",
				"Travel & Luggage",
				"Dining & nightlife",
				"Furniture",
				"Health & wellness",
				"Automotive & car",
				"For Man",
				"For Woman",
				"For Kids",
				"Pets",
				"Others",
		};
		for(String s : cats) {
			Category cat = new Category();
			cat.setName(s);
			create(cat);
		}
		return new GenericResponse();
	}

	@Path("create")
	@POST
	public void create(Category cat) {
		categoryService.create(cat);
    }

}
