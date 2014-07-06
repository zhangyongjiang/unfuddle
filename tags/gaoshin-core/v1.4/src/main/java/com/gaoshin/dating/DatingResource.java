package com.gaoshin.dating;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.gaoshin.beans.Category;
import com.gaoshin.beans.Dimension;
import com.gaoshin.beans.DimensionList;
import com.gaoshin.beans.DimensionValue;
import com.gaoshin.beans.DimensionValueList;
import com.gaoshin.beans.User;
import com.gaoshin.business.ObjectService;
import com.gaoshin.business.UserService;
import com.gaoshin.webservice.GaoshinResource;
import com.gaoshin.webservice.UserResource;
import com.sun.jersey.spi.inject.Inject;

@Path("/dating")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json;charset=utf-8" })
public class DatingResource extends GaoshinResource {
    @Inject
    private ObjectService objectService;

    @Inject
    private UserService userService;

    @Inject
    private DatingService datingService;

    @GET
    @Path("dimensions")
    public DimensionList getDatingUserDimensions() {
        Category category = objectService.getCategoryByName("Dating");
        DimensionList list = new DimensionList();
        list.setList(category.getDimensions());
        return list;
    }

    @GET
    @Path("my-profile")
    public DimensionValueList getMyDatingDimensionValues() {
        DimensionValueList userDimensions = getResource(UserResource.class).getUserDimensions();
        DimensionList datingDims = getDatingUserDimensions();
        for (Dimension dim : datingDims.getList()) {
            boolean found = false;
            for (DimensionValue dv : userDimensions.getList()) {
                if (dv.getDimension().getId().equals(dim.getId())) {
                    found = true;
                    dv.setDimension(dim);
                }
            }
            if (!found) {
                DimensionValue dv = new DimensionValue();
                dv.setDimension(dim);
                userDimensions.getList().add(dv);
            }
        }
        return userDimensions;
    }

    @POST
    @Path("signup")
    public User signup(DatingUser user) {
        return datingService.signup(user);
    }
}
