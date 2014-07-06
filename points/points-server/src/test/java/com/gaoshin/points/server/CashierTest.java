package com.gaoshin.points.server;

import org.junit.Before;
import org.junit.Test;

import com.gaoshin.points.server.bean.Cashier;
import com.gaoshin.points.server.bean.User;

public class CashierTest extends PointsResourceTester {
	private User merchant = null;
	private User customer = null;
	
	@Before
	public void createMerchant() {
		customer = createUser(false);
		merchant = createUser(false);
		
		User superUser = createUser(true);
		login(superUser);
    	getBuilder("/merchant/set-as-merchant/" + merchant.getId()).post(String.class, " ");
		logout();
	}
	
	@Test
	public void testCreateCashier() {
		login(merchant);
		
		Cashier cashier = new Cashier();
		String name = getCurrentTimeMillisString();
		cashier.setName(name);
		cashier.setCashierMerchantId(name);
		cashier.setPassword(name);
		
		getBuilder("/merchant/add-cashier").post(cashier);
	}
}
