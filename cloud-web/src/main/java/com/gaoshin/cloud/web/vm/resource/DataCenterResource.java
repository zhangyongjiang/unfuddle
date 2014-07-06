package com.gaoshin.cloud.web.vm.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.cloud.web.bean.GenericResponse;
import com.gaoshin.cloud.web.vm.bean.DataCenter;
import com.gaoshin.cloud.web.vm.bean.DataCenterDetails;
import com.gaoshin.cloud.web.vm.bean.DataCenterList;
import com.gaoshin.cloud.web.vm.bean.Pod;
import com.gaoshin.cloud.web.vm.bean.PodDetails;
import com.gaoshin.cloud.web.vm.service.DataCenterService;
import common.util.web.JerseyBaseResource;

@Path("/data-center")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class DataCenterResource extends JerseyBaseResource {
    @Autowired private DataCenterService dataCenterService;

    @POST
    @Path("/create")
    public DataCenter create(DataCenter dataCenter) {
        return dataCenterService.createDataCenter(dataCenter);
    }
    
    @POST
    @Path("/update")
    public DataCenter update(DataCenter dataCenter) {
        return dataCenterService.updateDataCenter(dataCenter);
    }
    
    @POST
    @Path("/delete/{dataCenterId}")
    public GenericResponse delete(@PathParam("dataCenterId") Long dataCenterId) {
        dataCenterService.deleteDataCenter(dataCenterId);
        return new GenericResponse();
    }
    
    @GET
    @Path("/details")
    public DataCenterDetails getDataCenterDetails(@QueryParam("dataCenterId")Long dataCenterId) {
        return dataCenterService.getDataCenterDetails(dataCenterId);
    }
    
    @GET
    @Path("/list")
    public DataCenterList listDataCenters() {
        return dataCenterService.listDataCenters();
    }

    @POST
    @Path("/pod/create")
    public Pod createPod(Pod pod) {
        return dataCenterService.createPod(pod);
    }
    
    @POST
    @Path("/pod/update")
    public Pod update(Pod pod) {
        return dataCenterService.updatePod(pod);
    }
    
    @POST
    @Path("/pod/delete/{podId}")
    public GenericResponse deletePod(@PathParam("podId") Long podId) {
        dataCenterService.deletePod(podId);
        return new GenericResponse();
    }
    
    @GET
    @Path("/pod/details")
    public PodDetails getPodDetails(@QueryParam("podId")Long podId) {
        return dataCenterService.getPodDetails(podId);
    }
    
}
