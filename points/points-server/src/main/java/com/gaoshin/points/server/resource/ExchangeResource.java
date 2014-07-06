package com.gaoshin.points.server.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gaoshin.points.server.bean.ExchangeHistory;
import com.gaoshin.points.server.service.ExchangeService;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/exchange")
@Component
@Scope("request")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class ExchangeResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(ExchangeResource.class.getName());

	@Inject private ExchangeService exchangeService;
	
	@POST
	@Path("/set-points/{itemId}/{points}")
	public GenericResponse setPoints(@PathParam("itemId") String itemId, int points) {
		String superUserId = assertRequesterUserId();
		exchangeService.setPoints(superUserId, itemId, points);
		return new GenericResponse();
	}
	
	@Path("/trade-points")
	@POST
	public ExchangeHistory tradePoints(ExchangeHistory trade) {	 
		String userId = assertRequesterUserId();
		return exchangeService.tradePoints(userId, trade);
	}
	
	
}
