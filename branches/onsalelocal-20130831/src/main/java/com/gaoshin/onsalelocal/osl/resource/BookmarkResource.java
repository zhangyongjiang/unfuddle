package com.gaoshin.onsalelocal.osl.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;

import com.gaoshin.onsalelocal.osl.beans.BookmarkDetails;
import com.gaoshin.onsalelocal.osl.beans.BookmarkDetailsList;
import com.gaoshin.onsalelocal.osl.entity.Bookmark;
import com.gaoshin.onsalelocal.osl.entity.BookmarkType;
import com.gaoshin.onsalelocal.osl.service.BookmarkService;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/bookmark")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class BookmarkResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(BookmarkResource.class.getName());

    @Inject private BookmarkService bookmarkService;
	
    @POST
    @Path("/add")
    public Bookmark add(Bookmark bookmark) {
    	String userId = assertRequesterUserId();
    	bookmark.setUserId(userId);
    	return bookmarkService.add(bookmark);
    }
    
    @POST
    @Path("/remove-by-id/{id}")
    public GenericResponse removeById(@PathParam("id") String id) {
    	String userId = assertRequesterUserId();
    	bookmarkService.removeBookmarkById(userId, id);
    	return new GenericResponse();
    }
    
    @POST
    @Path("/remove-by-offer-id/{offerid}")
    public GenericResponse removeByOfferId(@PathParam("offerid") String offerId) {
    	String userId = assertRequesterUserId();
    	bookmarkService.removeBookmarkByOfferId(userId, offerId);
    	return new GenericResponse();
    }
    
    @POST
    @Path("/remove-all")
    public GenericResponse removeAll() {
    	String userId = assertRequesterUserId();
    	bookmarkService.removeAll(userId);
    	return new GenericResponse();
    }
    
    @GET
    @Path("/list")
    public List<Bookmark> list(@QueryParam("type") BookmarkType type) {
    	String userId = assertRequesterUserId();
    	return bookmarkService.list(userId, type);
    }
    
    @GET
    @Path("/list-details")
    public BookmarkDetailsList listDetails(@QueryParam("type") BookmarkType type) {
    	String userId = assertRequesterUserId();
    	List<BookmarkDetails> list = bookmarkService.listDetails(userId, type);
    	BookmarkDetailsList result = new BookmarkDetailsList();
    	result.setItems(list);
		return result;
    }
}
