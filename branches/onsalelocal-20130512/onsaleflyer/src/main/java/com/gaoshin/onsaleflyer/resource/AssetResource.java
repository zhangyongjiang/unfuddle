package com.gaoshin.onsaleflyer.resource;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsaleflyer.beans.PosterId;
import com.gaoshin.onsaleflyer.beans.UserAsset;
import com.gaoshin.onsaleflyer.beans.PosterOwner;
import com.gaoshin.onsaleflyer.service.AssetService;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import common.util.JacksonUtil;
import common.util.web.BusinessException;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;
import common.util.web.ServiceError;

@Path("/ws/asset")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
@Component
public class AssetResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(AssetResource.class.getName());

    @Autowired private AssetService assetService;

    @Path("upload/{ownerId}/{groupId}/{artifactId}/{version}")
    @POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public GenericResponse uploadFile(
			@PathParam("ownerId") String ownerId,
			@PathParam("groupId") String groupId,
			@PathParam("artifactId") String artifactId,
			@PathParam("version") int version,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail)  {
    	String userId = assertRequesterUserId();
    	UserAsset ua = new UserAsset(new PosterOwner(ownerId, new PosterId(groupId, artifactId, version)), fileDetail.getFileName());
    	try {
			assetService.upload(userId, ua, uploadedInputStream);
		} catch (IOException e) {
			throw new BusinessException(ServiceError.Unknown, e);
		}
    	return new GenericResponse();
    }

    @Path("upload")
    @POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public GenericResponse uploadFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("json") String  json
			)  {
    	String userId = assertRequesterUserId();
    	PosterOwner up = null;
		try {
			up = JacksonUtil.json2Object(json, PosterOwner.class);
		} catch (Exception e1) {
			throw new BusinessException(ServiceError.InvalidInput, e1);
		}
    	UserAsset ua = new UserAsset(up, fileDetail.getFileName());
    	try {
			assetService.upload(userId, ua, uploadedInputStream);
		} catch (IOException e) {
			throw new BusinessException(ServiceError.IOError, e);
		}
    	return new GenericResponse();
    }

    @Path("upload/{groupId}/{artifactId}/{version}")
    @POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public GenericResponse uploadFile(
			@PathParam("groupId") String groupId,
			@PathParam("artifactId") String artifactId,
			@PathParam("version") int version,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail)  {
    	String userId = assertRequesterUserId();
    	return uploadFile(userId, groupId, artifactId, version, uploadedInputStream, fileDetail);
    }

    @Path("rename/{ownerId}/{groupId}/{artifactId}/{version}/{oldName}/{newName}")
    @POST
	public GenericResponse rename(
			@PathParam("ownerId") String ownerId,
			@PathParam("groupId") String groupId,
			@PathParam("artifactId") String artifactId,
			@PathParam("version") int version,
			@PathParam("oldName") String oldName,
			@PathParam("newName") String newName
			)  {
    	String userId = assertRequesterUserId();
    	UserAsset ua = new UserAsset(new PosterOwner(ownerId, new PosterId(groupId, artifactId, version)), oldName);
    	try {
			assetService.rename(userId, ua, newName);
		} catch (IOException e) {
			throw new BusinessException(ServiceError.IOError, e);
		}
    	return new GenericResponse();
    }

    @Path("rename/{groupId}/{artifactId}/{version}/{oldName}/{newName}")
    @POST
	public GenericResponse rename(
			@PathParam("groupId") String groupId,
			@PathParam("artifactId") String artifactId,
			@PathParam("version") int version,
			@PathParam("oldName") String oldName,
			@PathParam("newName") String newName
			)  {
    	String userId = assertRequesterUserId();
    	return rename(userId, groupId, artifactId, version, oldName, newName);
    }

    @Path("remove/{ownerId}/{groupId}/{artifactId}/{version}/{name}")
    @POST
	public GenericResponse remove(
			@PathParam("ownerId") String ownerId,
			@PathParam("groupId") String groupId,
			@PathParam("artifactId") String artifactId,
			@PathParam("version") int version,
			@PathParam("name") String name
			)  {
    	String userId = assertRequesterUserId();
    	UserAsset ua = new UserAsset(new PosterOwner(ownerId, new PosterId(groupId, artifactId, version)), name);
		assetService.remove(userId, ua);
    	return new GenericResponse();
    }

    @Path("remove/{groupId}/{artifactId}/{version}/{name}")
    @POST
	public GenericResponse remove(
			@PathParam("groupId") String groupId,
			@PathParam("artifactId") String artifactId,
			@PathParam("version") int version,
			@PathParam("name") String name
			)  {
    	String userId = assertRequesterUserId();
    	return remove(userId, groupId, artifactId, version, name);
    }

    @Path("get/{ownerId}/{groupId}/{artifactId}/{version}/{name}")
    @GET
	public Response get(
			@PathParam("ownerId") String ownerId,
			@PathParam("groupId") String groupId,
			@PathParam("artifactId") String artifactId,
			@PathParam("version") int version,
			@PathParam("name") String name
			)  {
    	String userId = assertRequesterUserId();
    	UserAsset ua = new UserAsset(new PosterOwner(ownerId, new PosterId(groupId, artifactId, version)), name);
		try {
			assetService.get(userId, ua, this.responseInvoker.get().getOutputStream());
		} catch (IOException e) {
			throw new BusinessException(ServiceError.IOError, e);
		}
    	return Response.ok().build();
    }

    @Path("get/{groupId}/{artifactId}/{version}/{name}")
    @GET
	public Response get(
			@PathParam("groupId") String groupId,
			@PathParam("artifactId") String artifactId,
			@PathParam("version") int version,
			@PathParam("name") String name
			)  {
    	String userId = assertRequesterUserId();
    	return get(userId, groupId, artifactId, version, name);
    }
}
