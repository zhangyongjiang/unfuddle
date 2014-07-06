package com.gaoshin.points.server;

import junit.framework.Assert;

import org.junit.Test;

import com.gaoshin.points.server.bean.User;

public class UserResourceTest extends PointsResourceTester {
	
	@Test
	public void testLogin(){
		User user = createUser(false);
	    User login = login(user);
		Assert.assertNotNull(user.getId());
		Assert.assertEquals(user.getPhone(), login.getPhone());
	}
	
}
