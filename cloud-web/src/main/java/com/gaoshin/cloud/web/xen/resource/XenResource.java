package com.gaoshin.cloud.web.xen.resource;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.cloud.web.bean.GenericResponse;
import com.gaoshin.cloud.web.xen.bean.CloneRequest;
import com.gaoshin.cloud.web.xen.bean.ConsoleSession;
import com.gaoshin.cloud.web.xen.bean.Host;
import com.gaoshin.cloud.web.xen.bean.HostDetails;
import com.gaoshin.cloud.web.xen.bean.HostList;
import com.gaoshin.cloud.web.xen.bean.HostNetworkList;
import com.gaoshin.cloud.web.xen.bean.SnapshotRequest;
import com.gaoshin.cloud.web.xen.bean.StorageRepoDetails;
import com.gaoshin.cloud.web.xen.bean.StorageRepoList;
import com.gaoshin.cloud.web.xen.bean.VirtualDiskImageList;
import com.gaoshin.cloud.web.xen.bean.VmDetails;
import com.gaoshin.cloud.web.xen.bean.VmList;
import com.gaoshin.cloud.web.xen.service.SessionManager;
import com.gaoshin.cloud.web.xen.service.XenService;
import common.util.web.JerseyBaseResource;

@Path("/xen")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class XenResource extends JerseyBaseResource {
    @Autowired private XenService xenService;
    @Autowired private SessionManager sessionManager;

    @GET
    @Path("/host/details/{id}")
    public HostDetails get(@PathParam("id") Long hostId) {
        return xenService.getHostDetails(hostId);
    }
    
    @POST
    @Path("/host/add")
    public Host create(Host host) {
        return xenService.createHost(host);
    }
    
    @POST
    @Path("/host/update")
    public GenericResponse update(Host host) {
        xenService.updateHost(host);
        return new GenericResponse();
    }
    
    @POST
    @Path("/host/remove/{id}")
    public GenericResponse remove(@PathParam("id") Long hostId) {
        xenService.removeHost(hostId);
        return new GenericResponse();
    }
    
    @GET
    @Path("/host/list")
    public HostList listHosts() {
        return xenService.listHosts();
    }
    
    @POST
    @Path("/host/sr/refresh")
    public GenericResponse refreshStorageRepository(@QueryParam("hostId") Long hostId) throws Exception {
        xenService.refreshHostStorageRepository(hostId);
        return new GenericResponse();
    }
    
    @GET
    @Path("/host/vm/list")
    public VmList listVms(@QueryParam("hostId") Long hostId) throws Exception {
        return xenService.listHostVms(hostId);
    }
    
    @GET
    @Path("/host/vm/details")
    public VmDetails getVmDetails(@QueryParam("hostId") Long hostId, @QueryParam("vmId") String vmId) throws Exception {
        return xenService.getVmDetails(hostId, vmId);
    }
    
    @POST
    @Path("/host/vm/start/{hostId}/{vmId}")
    public GenericResponse startVm(@PathParam("hostId") Long hostId, @PathParam("vmId") String vmId) throws Exception {
        xenService.startVm(hostId, vmId);
        return new GenericResponse();
    }
    
    @POST
    @Path("/host/vm/suspend/{hostId}/{vmId}")
    public GenericResponse suspendVm(@PathParam("hostId") Long hostId, @PathParam("vmId") String vmId) throws Exception {
        xenService.suspendVm(hostId, vmId);
        return new GenericResponse();
    }
    
    @POST
    @Path("/host/vm/shutdown/{hostId}/{vmId}")
    public GenericResponse stopVm(@PathParam("hostId") Long hostId, @PathParam("vmId") String vmId) throws Exception {
        xenService.shutdownVm(hostId, vmId);
        return new GenericResponse();
    }
    
    @POST
    @Path("/host/vm/resume/{hostId}/{vmId}")
    public GenericResponse resumeVm(@PathParam("hostId") Long hostId, @PathParam("vmId") String vmId) throws Exception {
        xenService.resumeVm(hostId, vmId);
        return new GenericResponse();
    }
    
    @POST
    @Path("/host/vm/destroy/{hostId}/{vmId}")
    public GenericResponse destroyVm(@PathParam("hostId") Long hostId, @PathParam("vmId") String vmId) throws Exception {
        xenService.destroyVm(hostId, vmId);
        return new GenericResponse();
    }
    
    @POST
    @Path("/host/vm/clone")
    public GenericResponse cloneVm(CloneRequest cloneRequest) throws Exception {
        String uuid = xenService.cloneVm(cloneRequest);
        GenericResponse response = new GenericResponse();
        response.setData(uuid);
        return response;
    }
    
    @POST
    @Path("/host/vm/snapshot")
    public GenericResponse snapshotVm(SnapshotRequest cloneRequest) throws Exception {
        String uuid = xenService.snapshotVm(cloneRequest);
        GenericResponse response = new GenericResponse();
        response.setData(uuid);
        return response;
    }
    
    @GET
    @Path("/host/vm/session/console/open")
    public ConsoleSession getConsole(@QueryParam("hostId") Long hostId, @QueryParam("vmId") String vmId, @QueryParam("consoleId") String consoleId) throws Exception {
        return xenService.getConsole(hostId, vmId, consoleId);
    }
    
    @GET
    @Path("/host/vm/session/heartbeat/{sessionId}")
    public Response sessionHeartBeat(@PathParam("sessionId") String sessionId) throws ServletException, IOException {
        xenService.sessionHeartBeat(sessionId);
        responseInvoker.get().setContentType("images/gif");
        requestInvoker.get().getRequestDispatcher("/images/1x1.gif").include(requestInvoker.get(), responseInvoker.get());
        return Response.ok().build();
    }
    
    @POST
    @Path("/host/vm/change-network/{hostId}/{vmId}")
    public GenericResponse changeNetwork(@PathParam("hostId") Long hostId, @PathParam("vmId") String vmId) throws Exception {
        xenService.changeNetwork(hostId, vmId);
        return new GenericResponse();
    }
    
    @GET
    @Path("/host/sr/details")
    public StorageRepoDetails getStorageRepoDetails(@QueryParam("hostId") Long hostId, @QueryParam("srUuid") String srUuid) throws Exception {
        return xenService.getStorageRepoDetails(hostId, srUuid);
    }
    
    @GET
    @Path("/host/vdi-list")
    public VirtualDiskImageList listHostVdis(@QueryParam("hostId") Long hostId) throws Exception {
        return xenService.listHostVdis(hostId);
    }
    
    @GET
    @Path("/host/sr-list")
    public StorageRepoList listStorage(@QueryParam("hostId") Long hostId) throws Exception {
        return xenService.listHostStorage(hostId);
    }
    
    @GET
    @Path("/host/network-list")
    public HostNetworkList listNetwork(@QueryParam("hostId") Long hostId) throws Exception {
        return xenService.listNetwork(hostId);
    }
}
