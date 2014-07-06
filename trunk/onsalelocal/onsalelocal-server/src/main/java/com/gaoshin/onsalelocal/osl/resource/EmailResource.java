package com.gaoshin.onsalelocal.osl.resource;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;

import com.gaoshin.onsalelocal.osl.service.ContentService;
import com.gaoshin.onsalelocal.osl.service.EmailService;
import com.gaoshin.onsalelocal.osl.service.OslService;
import com.gaoshin.onsalelocal.osl.service.UserService;
import com.nextshopper.osl.entity.EmailOffer;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/email")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class EmailResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(EmailResource.class.getName());

    @Inject private OslService oslService;
    @Inject private ContentService contentService;
    @Inject private UserService userService;
    @Inject private EmailService emailService;

    @POST
    @Path("/process")
    public GenericResponse processEmail(@QueryParam("threshold") @DefaultValue("4") final int threshold) throws Exception{
    	new Thread(new Runnable() {
			@Override
			public void run() {
		    	emailService.process(false, threshold);
			}
		}).start();
    	return new GenericResponse();
    }
    
    @POST
    @Path("/process-and-delete")
    public GenericResponse processAndDeleteEmail(@QueryParam("threshold") @DefaultValue("4") final int threshold) throws Exception{
    	new Thread(new Runnable() {
			@Override
			public void run() {
		    	emailService.process(true, threshold);
			}
		}).start();
    	return new GenericResponse();
    }
    
    @GET
    @Path("/list")
    public List<EmailOffer> list(@QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("size") @DefaultValue("100") int size) {
    	return emailService.list(offset, size);
    }
    
    @GET
    @Path("/details")
    public EmailOffer list(@QueryParam("id") String id) {
    	return emailService.getEmailOfferDetails(id);
    }
}
