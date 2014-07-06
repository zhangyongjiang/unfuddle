package com.gaoshin.webservice;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import com.gaoshin.beans.GenericResponse;
import com.gaoshin.beans.Location;
import com.gaoshin.beans.LocationList;
import com.gaoshin.beans.User;
import com.gaoshin.business.LocationService;
import com.gaoshin.business.UserService;
import com.sun.jersey.spi.inject.Inject;
import common.web.BusinessException;
import common.web.ServiceError;

@Path("/location")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json;charset=utf-8" })
public class LocationResource extends GaoshinResource {
	
    @Inject
    private LocationService locationService;

    @Inject
    private UserService userService;

    @POST
    @Path("new")
    public Location save(Location location) {
        UserResource userResource = getResource(UserResource.class);
        User user = userResource.assertCurrentUser();
        if (userService.checkUser(user) == null)
            throw new BusinessException(ServiceError.NoGuest);
        return locationService.save(user, location);
    }

    @POST
    @Path("current/{id}")
    public GenericResponse setCurrentLocation(@PathParam("id") long locationId) {
        UserResource userResource = getResource(UserResource.class);
        User user = userResource.assertCurrentUser();
        locationService.setUserCurrentLocation(user, locationId);
        return new GenericResponse();
    }

	@GET
	@Path("{id}")
	public Location get(@PathParam("id")Long id) {
        Location location = locationService.get(id);
        return location;
	}

    @GET
    @Path("my")
    public LocationList getUserLocationList() {
        UserResource userResource = getResource(UserResource.class);
        User user = userResource.assertCurrentUser();
        return locationService.getUserLocationList(user);
    }

    @GET
    @Path("device")
    public LocationList getUserDeviceLocationList(
            @DefaultValue("0") @QueryParam("offset") int offset,
            @DefaultValue("10") @QueryParam("size") int size
            ) {
        if (size > 100)
            size = 100;
        UserResource userResource = getResource(UserResource.class);
        User user = userResource.assertCurrentUser();
        return locationService.getUserDeviceLocationList(user, offset, size);
    }
}
