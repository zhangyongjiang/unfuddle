package com.gaoshin.points.server;

import junit.framework.Assert;

import org.junit.Test;

import com.gaoshin.points.server.bean.Item;
import com.gaoshin.points.server.bean.ItemList;
import com.gaoshin.points.server.bean.User;
import com.gaoshin.points.server.entity.UserType;
import com.sun.jersey.api.client.UniformInterfaceException;
import common.util.web.ServiceError;

public class MerchantResourceTest extends PointsResourceTester {
	
	@Test
	public void testSetAsMerchant() {
		User merchant = createUser(false);
		login(merchant);
		User u = getUserById(merchant.getId());
		Assert.assertEquals(UserType.Individual, u.getUserType());
		
		try {
	    	getBuilder("/merchant/set-as-merchant/" + merchant.getId()).post(String.class, " ");
			Assert.assertTrue(false);
		} catch (UniformInterfaceException e) {
			Assert.assertEquals(ServiceError.PermissionDenied.getCode(), e.getResponse().getStatus());
		}

		logout();
		User superUser = createUser(true);
		login(superUser);
		
    	getBuilder("/merchant/set-as-merchant/" + merchant.getId()).post(String.class, " ");
		User u2 = getUserById(merchant.getId());
		Assert.assertEquals(UserType.Merchant, u2.getUserType());
		logout();
		
		Item item = new Item();
		item.setStartTime(0);
		item.setName("DEFAULT");
		item.setExpire(Long.MAX_VALUE);
		try {
			getBuilder("/merchant/create-item").post(item);
			Assert.assertTrue(false);
		} catch (UniformInterfaceException e) {
			Assert.assertEquals(ServiceError.NoGuest.getCode(), e.getResponse().getStatus());
		}
		
		User login = login(merchant);
		Assert.assertEquals(UserType.Merchant, login.getUserType());
		getBuilder("/merchant/create-item").post(item);
		
		ItemList vl = getBuilder("/merchant/item/list/" + merchant.getId()).get(ItemList.class);
		Assert.assertEquals(1, vl.getList().size());
		
		Item merchantDefaultItem = getBuilder("/merchant/item/" + vl.getList().get(0).getId()).get(Item.class);
		Assert.assertEquals(0, merchantDefaultItem.getStartTime());
		Assert.assertEquals(item.getName(), merchantDefaultItem.getName());
		Assert.assertEquals(Long.MAX_VALUE, vl.getList().get(0).getExpire());
	}
}
