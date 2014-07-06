package com.gaoshin.points.server.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gaoshin.points.server.bean.Cashier;
import com.gaoshin.points.server.bean.ExchangeHistory;
import com.gaoshin.points.server.bean.Item;
import com.gaoshin.points.server.bean.ItemList;
import com.gaoshin.points.server.bean.User;
import com.gaoshin.points.server.entity.UserType;
import com.gaoshin.points.server.service.MerchantService;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.BusinessException;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;
import common.util.web.ServiceError;

@Path("/merchant")
@Component
@Scope("request")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class MerchantResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(MerchantResource.class.getName());

	@Inject private MerchantService merchantService;

	@POST
	@Path("/set-as-merchant/{userId}")
	public GenericResponse setAsMerchant(@PathParam("userId") String userId) {
		UserResource userResource = getResource(UserResource.class);
		User me = userResource.me();
		if(!UserType.Super.equals(me.getUserType())) {
			throw new BusinessException(ServiceError.PermissionDenied);
		}
		merchantService.setAsMerchant(userId);
		return new GenericResponse();
	}
	
	@Path("/create-item")
	@POST
	public Item createItem(Item item) {	 
		String userId = assertRequesterUserId();
		return merchantService.createItem(userId, item);
	}

	@Path("/add-cashier")
	@POST
	public Cashier addCashier(Cashier cashier) {	 
		String userId = assertRequesterUserId();
		return merchantService.addCashier(userId, cashier);
	}

	@Path("/item/{itemId}")
	@GET
	public Item getItemById(@PathParam("itemId") String itemId) {
		return merchantService.getItemById(itemId);
	}

	@Path("/item/list/{merchantId}")
	@GET
	public ItemList list(@PathParam("merchantId") String merchantId) {
		return merchantService.listVarieties(merchantId);
	}
	
	@Path("/adjust-points")
	@POST
	public ExchangeHistory adjustPoints(ExchangeHistory trade) {	 
		String cashierId = assertRequesterUserId();
		return merchantService.adjustPoints(cashierId, trade);
	}
	
	@Path("/adjust-points-by-phone/{phone}")
	@POST
	public ExchangeHistory adjustPoints(@PathParam("phone") String phone, ExchangeHistory trade) {	 
		String cashierId = assertRequesterUserId();
		return merchantService.adjustPointsByPhone(cashierId, phone, trade);
	}
}
