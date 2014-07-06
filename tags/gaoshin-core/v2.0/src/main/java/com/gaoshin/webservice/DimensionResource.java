package com.gaoshin.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.gaoshin.beans.Dimension;
import com.gaoshin.beans.DimensionValue;
import com.gaoshin.beans.GenericResponse;
import com.gaoshin.business.ObjectService;
import com.sun.jersey.spi.inject.Inject;

@Path("/dimension")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json;charset=utf-8" })
public class DimensionResource extends GaoshinResource {
	
    @Inject
    private ObjectService objectService;

    @POST
    @Path("create")
    public Dimension save(Dimension dimension) {
        return objectService.createDimension(dimension);
    }

    @GET
    @Path("{dimensionId}")
    public Dimension getDimension(@PathParam("dimensionId") Long dimensionId) {
        return objectService.getDimension(dimensionId);
    }

    @POST
    @Path("remove/{dimid}")
    public GenericResponse remove(@PathParam("dimid") Long dimensionId) {
        objectService.removeDimension(dimensionId);
        return new GenericResponse();
    }

    @POST
    @Path("add-dimension-value/{dimId}")
    public DimensionValue addDimensionValue(@PathParam("dimId") Long dimensionId, DimensionValue dimensionValue) {
        return objectService.addDimensionValue(dimensionId, dimensionValue);
    }

    @POST
    @Path("remove-dimension-value/{dimensionValueId}")
    public GenericResponse removeDimensionValue(@PathParam("dimensionValueId") Long dimensionValueId) {
        objectService.removeDimensionValue(dimensionValueId);
        return new GenericResponse();
    }
}
