package com.gaoshin.coupon.webservice;

import org.junit.Assert;
import org.junit.Test;

import com.gaoshin.coupon.entity.User;
import com.gaoshin.coupon.entity.UserType;
import com.sun.jersey.api.client.UniformInterfaceException;
import common.util.web.ServiceError;

public class TransactionTest extends CouponeTestBase {
    @Test
    public void testTransaction() {
        String login = "transaction-test";
        String pwd = getCurrentTimeMillisString();
        try {
            register(UserType.Individual, login, pwd);
            Assert.assertTrue(false);
        }
        catch (Exception e) {
        }
        
        User user = new User();
        user.setLogin(login);
        user.setPassword(pwd);
        try {
            login(user );
            Assert.assertTrue(false);
        }
        catch (UniformInterfaceException e) {
            Assert.assertEquals(ServiceError.NotFound.getErrorCode(), e.getResponse().getStatus());
        }
    }
}
