package com.gaoshin.onsaleflyer.resource;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsaleflyer.service.OfferService;
import common.util.web.JerseyBaseResource;

@Path("/ws/offer")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
@Component
public class OfferResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(OfferResource.class.getName());

    @Autowired private OfferService offerService;
	
}
