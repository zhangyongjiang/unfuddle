package com.gaoshin.onsalelocal.osl.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.osl.entity.StoreDetails;
import com.gaoshin.onsalelocal.osl.entity.UserFile;
import com.gaoshin.onsalelocal.osl.service.ContentService;
import com.gaoshin.onsalelocal.osl.service.OslService;
import com.gaoshin.onsalelocal.osl.service.UserService;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.BusinessException;
import common.util.web.JerseyBaseResource;
import common.util.web.ServiceError;

@Path("/ws/store")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class StoreResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(StoreResource.class.getName());

    @Inject private OslService oslService;
    @Inject private ContentService contentService;
    @Inject private UserService userService;

    @POST
    @Path("/create")
    public Store createStore(Store store) throws Exception{
    	String userId = assertRequesterUserId();
    	store.setCreated(System.currentTimeMillis());
    	return oslService.createStore(userId, store);
    }
    
    @POST
    @Path("/update")
    public Store updateStore(Store store) throws Exception{
    	String userId = assertRequesterUserId();
    	return oslService.updateStore(userId, store);
    }
    
    @POST
    @Path("/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public StoreDetails upload(@FormDataParam("storeId") String storeId, @FormDataParam("image") InputStream stream) throws Exception{
    	String userId = assertRequesterUserId();
    	StoreDetails storeDetails = oslService.getStoreDetails(storeId);
    	if(!userId.equals(storeDetails.getOwnerUserId()))
    		throw new BusinessException(ServiceError.NotAuthorized);
    	
    	UserFile userFile = contentService.save(userId, stream);
    	String imgId = userFile.getFileId();
    	
    	String requestURI = requestInvoker.get().getRequestURL().toString();
    	String imgUrl = requestURI + "/" + imgId;
    	storeDetails.setLogo(imgUrl);
    	oslService.updateStoreImageUrl(storeId, imgUrl);
    	
        return storeDetails;
    }
    
    @GET
    @Path("/image/{imgId}")
    public Response getAvatar(@PathParam("imgId") String imgId) throws IOException {
    	OutputStream outputStream = responseInvoker.get().getOutputStream();
    	contentService.read(imgId, outputStream);
    	return Response.ok().build();
    }
}
