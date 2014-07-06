package com.gaoshin.shopping;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import com.gaoshin.amazon.AwsBrowseNode;
import com.gaoshin.amazon.AwsItem;
import com.gaoshin.amazon.AwsService;
import com.gaoshin.amazon.ItemInterest;
import com.gaoshin.amazon.NodeInterest;
import com.gaoshin.amazon.NodeInterestList;
import com.gaoshin.beans.Category;
import com.gaoshin.beans.Location;
import com.gaoshin.beans.User;
import com.gaoshin.business.ObjectService;
import com.gaoshin.business.UserService;
import com.gaoshin.webservice.CategoryResource;
import com.gaoshin.webservice.GaoshinResource;
import com.gaoshin.webservice.ObjectResource;
import com.sun.jersey.spi.inject.Inject;

@Path("/shopping")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json;charset=utf-8" })
public class ShoppingResource extends GaoshinResource {
    @Inject
    private ShoppingService shoppingService;

    @Inject
    private ObjectService objectService;

    @Inject
    private UserService userService;

    @Inject
    private ShoppingConfiguration shoppingConfiguration;

    @Inject
    private AwsService awsService;

    @Path("category")
    public CategoryResource getCategory() {
        return getResource(CategoryResource.class);
    }

    @Path("object")
    public ObjectResource getObjectResource() {
        return getResource(ObjectResource.class);
    }

    @GET
    @Path("/create/need")
    public Category getNeedCategory() {
        return getResource(CategoryResource.class).getCategory(ShoppingConstant.ShoppingRootCategoryName);
    }

    @GET
    @Path("/create/need/{idOrName}")
    public Category getNeedCategory(@PathParam("idOrName") String idOrName) {
        return getResource(CategoryResource.class).getCategory(idOrName);
    }

    @GET
    @Path("/create/have/{categoryId}")
    public Category haveGet(@PathParam("categoryId") Long categoryId) {
        return getResource(CategoryResource.class).getCategory(categoryId + "");
    }

    @GET
    @Path("/amazon/{asin}")
    public AwsItem getItem(@PathParam("asin") String asin, @QueryParam("latitude") Float latitude,
            @QueryParam("longitude") Float longitude, @DefaultValue("1") @QueryParam("miles") int miles) {
        if (latitude == null || longitude == null) {
            User user = getUser();
            if (user == null)
                return awsService.getItem(asin);
            user = userService.getUser(user);
            Location location = user.getCurrentLocation();
            if (location == null)
                return awsService.getItem(asin);
            return awsService.getItem(asin, location.getLatitude(), location.getLongitude(), miles);
        }
        else
            return awsService.getItem(asin, latitude, longitude, miles);
    }

    @POST
    @Path("/amazon/need/{asin}")
    public AwsItem interest(@PathParam("asin") String asin) {
        ItemInterest interest = new ItemInterest();
        AwsItem item = new AwsItem();
        item.setAsin(asin);
        interest.setItem(item);
        interest.setSell(false);
        User user = assertUser();
        return awsService.addInterest(user, interest);
    }

    @POST
    @Path("/amazon/need/{asin}/{locationId}")
    public AwsItem need(@PathParam("asin") String asin, @PathParam("locationId") Long locationId) {
        ItemInterest interest = new ItemInterest();
        AwsItem item = new AwsItem();
        item.setAsin(asin);
        interest.setItem(item);
        interest.setSell(false);
        Location location = new Location();
        location.setId(locationId);
        interest.setLocation(location);
        User user = assertUser();
        return awsService.addInterest(user, interest);
    }

    @POST
    @Path("/amazon/have/{asin}/{locationId}")
    public AwsItem have(@PathParam("asin") String asin, @PathParam("locationId") Long locationId) {
        ItemInterest interest = new ItemInterest();
        AwsItem item = new AwsItem();
        item.setAsin(asin);
        interest.setItem(item);
        interest.setSell(true);
        Location location = new Location();
        location.setId(locationId);
        interest.setLocation(location);
        User user = assertUser();
        return awsService.addInterest(user, interest);
    }

    @POST
    @Path("/amazon/node/interest")
    public AwsBrowseNode createNodeInterest(NodeInterest nodeInterest) {
        User user = assertUser();
        nodeInterest.setUser(user);
        return awsService.addNodeInterest(nodeInterest);
    }

    @GET
    @Path("/amazon/node/interest")
    public NodeInterestList getNodeInterest(@QueryParam("id") String nodeId) {
        User user = assertUser();
        return awsService.getNodeInterest(user, nodeId);
    }

    @GET
    @Path("/amazon/node")
    public AwsBrowseNode getNode(@QueryParam("id") String nodeId) {
        User user = assertUser();
        return awsService.getBrowseNode(nodeId);
    }

    @POST
    @Path("/amazon/remove-interest/{id}")
    public AwsItem removeInterest(@PathParam("id") Long interestId) {
        User user = assertUser();
        return awsService.removeInterest(user, interestId);
    }
}
