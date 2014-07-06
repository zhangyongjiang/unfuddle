package com.gaoshin.webservice;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gaoshin.beans.ObjectBean;
import com.gaoshin.beans.ObjectBeanList;
import com.gaoshin.business.ObjectService;
import com.sun.jersey.spi.inject.Inject;

@Path("/object")
@Component
@Scope("request")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json;charset=utf-8" })
public class ObjectResource extends GaoshinResource {
	
	@Inject
    private ObjectService objectService;

	@POST
    @Path("create")
    public ObjectBean create(ObjectBean bean) {
        bean.setUser(assertUser());
        return objectService.createObject(bean);
	}
	
    @GET
    @Path("{id}")
    public ObjectBean get(@PathParam("id") Long objectId) {
        return objectService.getObjectBean(objectId);
    }

    @GET
    @Path("search")
    public ObjectBeanList search(
            @QueryParam("q") String keywords,
            @QueryParam("d") String dimValues,
            @QueryParam("lat") Float latitude,
            @QueryParam("lng") Float longitude,
            @DefaultValue("1") @QueryParam("range") Integer range,
            @DefaultValue("0") @QueryParam("offset") Integer offset,
            @DefaultValue("10") @QueryParam("size") Integer size
            ) {
        List<String> dimValueList = split(dimValues);
        if (range > 10)
            range = 10;
        return objectService.search(keywords, dimValueList, latitude, longitude, range, offset, size);
    }
}
