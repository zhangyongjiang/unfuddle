package com.gaoshin.onsalelocal.osl.resource;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.gaoshin.onsalelocal.osl.dao.OslDao;
import com.gaoshin.onsalelocal.osl.service.OslService;
import com.gaoshin.onsalelocal.osl.service.UserService;
import com.gaoshin.onsalelocal.osl.service.impl.OslServiceImpl;
import com.gaoshin.onsalelocal.osl.service.impl.UserServiceImpl;
import com.nextshopper.api.Offer;
import com.nextshopper.api.OfferList;
import com.sun.jersey.api.client.UniformInterfaceException;
import common.util.web.ServiceError;
import common.util.web.SpringResourceTester;

public class TrendResourceTest extends SpringResourceTester {
	@Test
	public void test() throws Exception {
		UserService proxy = getSpringBean(UserService.class);
		UserServiceImpl userService = getTargetObject(proxy, UserServiceImpl.class);
		
		OslService oslServiceProxy = getSpringBean(OslService.class);
		OslServiceImpl oslServiceImpl = getTargetObject(oslServiceProxy, OslServiceImpl.class);
		
		final List<Offer> trendOffers = new ArrayList<Offer>();
		Offer o1 = new Offer();
		o1.setMerchant(getCurrentTimeMillisString());
		trendOffers.add(o1);
		
		Offer o2 = new Offer();
		o2.setMerchant(getCurrentTimeMillisString());
		trendOffers.add(o2);
		
		OslDao olddao = oslServiceImpl.getOslDao();
		oslServiceImpl.setOslDao(new DummyOslDao() {
			@Override
			public List<Offer> trend(float lat, float lng, int radius,
			        int offset, int size) {
			    return trendOffers;
			}
		});
		
		try {
			OfferList offers = getBuilder("/ws/v2/trend").get(OfferList.class);
        } catch (UniformInterfaceException e) {
        	Assert.assertEquals(ServiceError.InvalidInput.getErrorCode(), e.getResponse().getStatus());
        }
		
		OfferList offers = getBuilder("/ws/v2/trend", "latitude", "37", "longitude", "-122").get(OfferList.class);
		Assert.assertEquals(2, offers.getItems().size());
		Assert.assertEquals(o1.getMerchant(), offers.getItems().get(0).getMerchant());
		Assert.assertEquals(o2.getMerchant(), offers.getItems().get(1).getMerchant());
	}
}
