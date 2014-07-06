package com.gaoshin.onsaleflyer.resource;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsaleflyer.service.FlyerService;
import common.util.web.JerseyBaseResource;

@Path("/ws/flyer")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
@Component
public class FlyerResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(FlyerResource.class.getName());

    @Autowired private FlyerService flyerService;
	
}
