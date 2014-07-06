package com.gaoshin.onsaleflyer.resource;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsaleflyer.beans.Poster;
import com.gaoshin.onsaleflyer.beans.PosterId;
import com.gaoshin.onsaleflyer.beans.PosterList;
import com.gaoshin.onsaleflyer.beans.PosterOwner;
import com.gaoshin.onsaleflyer.entity.Visibility;
import com.gaoshin.onsaleflyer.service.PosterService;
import common.util.web.BusinessException;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;
import common.util.web.ServiceError;

@Path("/ws/poster")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
@Component
public class PosterResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(PosterResource.class.getName());

    @Autowired private PosterService posterService;
	
    @Path("/create")
    @POST
    public GenericResponse create(Poster poster) throws IOException  {
    	String userId = assertRequesterUserId();
    	posterService.create(userId, poster);
    	return new GenericResponse();
    }
	
    @Path("delete/{ownerId}/{groupId}/{artifactId}/{version}")
    @POST
    public GenericResponse delete(
			@PathParam("ownerId") String ownerId,
			@PathParam("groupId") String groupId,
			@PathParam("artifactId") String artifactId,
			@PathParam("version") int version
			)  {
    	PosterOwner posterOwner = new PosterOwner(ownerId, new PosterId(groupId, artifactId, version));
    	return delete(posterOwner);
    }
	
    @Path("delete")
    @POST
    public GenericResponse delete(PosterOwner up)  {
    	String userId = assertRequesterUserId();
    	posterService.deletePoster(userId, up);
    	return new GenericResponse();
    }
	
    @Path("save")
    @POST
    public GenericResponse save(Poster poster) {
    	String userId = assertRequesterUserId();
    	try {
			posterService.save(userId, poster);
		} catch (IOException e) {
			throw new BusinessException(ServiceError.IOError, e);
		}
    	return new GenericResponse();
    }
	
    @Path("delete/{groupId}/{artifactId}/{version}")
    @POST
    public GenericResponse delete(
			@PathParam("groupId") String groupId,
			@PathParam("artifactId") String artifactId,
			@PathParam("version") int version
			)  {
    	String userId = assertRequesterUserId();
    	return delete(userId, groupId, artifactId, version);
    }
	
    @Path("list/{ownerId}")
    @GET
    public PosterList list(@PathParam("ownerId") String ownerId) {
    	String userId = getRequesterUserId();
    	try {
			return posterService.list(userId, ownerId);
		} catch (IOException e) {
			throw new BusinessException(ServiceError.IOError, e);
		}
    }
	
    @Path("list")
    @GET
    public PosterList list() {
    	String userId = assertRequesterUserId();
    	try {
			return posterService.list(userId, userId);
		} catch (IOException e) {
			throw new BusinessException(ServiceError.IOError, e);
		}
    }
	
    @Path("get/{ownerId}/{groupId}/{artifactId}/{version}")
    @GET
    public Poster get(
			@PathParam("ownerId") String ownerId,
			@PathParam("groupId") String groupId,
			@PathParam("artifactId") String artifactId,
			@PathParam("version") int version
			)  {
    	String userId = getRequesterUserId();
    	PosterOwner posterOwner = new PosterOwner(ownerId, new PosterId(groupId, artifactId, version));
    	try {
			return posterService.getPoster(userId, posterOwner);
		} catch (IOException e) {
			throw new BusinessException(ServiceError.IOError, e);
		}
    }
	
    @Path("get/{groupId}/{artifactId}/{version}")
    @GET
    public Poster get(
			@PathParam("groupId") String groupId,
			@PathParam("artifactId") String artifactId,
			@PathParam("version") int version
			)  {
    	String userId = getRequesterUserId();
    	return get(userId, groupId, artifactId, version);
    }

    @Path("set-visibility/{ownerId}/{groupId}/{artifactId}/{version}/{visibility}")
    @POST
	public GenericResponse setVisibility(
			@PathParam("ownerId") String ownerId,
			@PathParam("groupId") String groupId,
			@PathParam("artifactId") String artifactId,
			@PathParam("version") int version,
			@PathParam("visibility") Visibility visibility
			)  {
    	String userId = assertRequesterUserId();
    	PosterOwner posterOwner = new PosterOwner(ownerId, new PosterId(groupId, artifactId, version));
		try {
			posterService.setVisibility(userId, posterOwner, visibility);
		} catch (IOException e) {
			throw new BusinessException(ServiceError.IOError, e);
		}
    	return new GenericResponse();
    }
}
