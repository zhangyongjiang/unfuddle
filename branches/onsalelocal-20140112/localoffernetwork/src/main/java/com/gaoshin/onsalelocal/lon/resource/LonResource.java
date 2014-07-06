package com.gaoshin.onsalelocal.lon.resource;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.lon.service.LonService;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.JerseyBaseResource;

@Path("/ws/groupon")
@Component
public class LonResource extends JerseyBaseResource {
    @Inject private LonService grouponService;

}
