package com.gaoshin.onsalelocal.osl.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.gaoshin.onsalelocal.api.Store;
import com.gaoshin.onsalelocal.osl.entity.StoreDetails;
import com.gaoshin.onsalelocal.osl.entity.UserFile;
import com.gaoshin.onsalelocal.osl.service.ContentService;
import com.gaoshin.onsalelocal.osl.service.OslService;
import com.gaoshin.onsalelocal.osl.service.SearchService;
import com.gaoshin.onsalelocal.osl.service.UserService;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.inject.Inject;
import common.crawler.CrawlerBase;
import common.util.StringUtil;
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
    @Inject private SearchService searchService;

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
    
    @GET
    @Path("search")
    @Produces("application/json")
    public Response search(@QueryParam("merchant") String merchant, @QueryParam("city") String city, @QueryParam("phone") String phone) throws IOException {
    	merchant = StringUtil.nullIfEmpty(merchant);
    	city = StringUtil.nullIfEmpty(city);
    	phone = StringUtil.nullIfEmpty(phone);
    	String text = "";
    	String query = "";
    	if(merchant!=null) {
    		merchant = merchant.replaceAll("[^a-zA-Z0-9 ]+", "");
    		text += merchant;
    	}
    	if(city != null )
    		text += " " + city;
    	text = text.trim();
    	if(text.length()>0)
    		query = "text:(" + URLEncoder.encode(text) + ")";
    	if(phone != null){
    		phone = CrawlerBase.formatPhone(phone);
			query = "+phone:" + phone + "";
    	}
    	searchService.searchStore("q=" + query + "&wt=json", responseInvoker.get().getOutputStream());
    	return Response.ok().build();
    }
}
