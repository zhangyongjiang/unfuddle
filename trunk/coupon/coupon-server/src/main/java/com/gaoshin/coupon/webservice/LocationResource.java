package com.gaoshin.coupon.webservice;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;

import com.gaoshin.coupon.bean.City;
import com.gaoshin.coupon.bean.CityList;
import com.gaoshin.coupon.db.UserContextHolder;
import com.gaoshin.coupon.service.LocationService;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/location")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class LocationResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(LocationResource.class.getName());

    @Inject private LocationService locationService;
	
    @GET
    @Path("/set-location/{location}")
    public GenericResponse setLocation(@PathParam("location") String location) {
        setCookie("LOCATION", location);
        UserContextHolder.setUserState(location);
        return new GenericResponse();
    }

    @GET
    @Path("/set-location/{latitude}/{longitude}")
    public CityList setLocation(@PathParam("latitude") float latitude, @PathParam("longitude") float longitude) {
        List<City> cities = locationService.listNearbyCities(latitude, longitude, 50);
        if(cities.size() > 0) {
            setLocation(cities.get(0).getState());
        }
        CityList list = new CityList();
        list.setItems(cities);
        return list;
    }

    @GET
    @Path("/nearby-cities")
    public CityList listNearbyCities(
            @QueryParam("latitude") float latitude
            , @QueryParam("longitude") float longitude
            , @QueryParam("radius") @DefaultValue("50") float radius) {
        List<City> cities = locationService.listNearbyCities(latitude, longitude, radius);
        CityList list = new CityList();
        list.setItems(cities);
        return list;
    }
}
