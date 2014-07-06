package com.gaoshin.cloud.web;

import java.io.IOException;

import junit.framework.Assert;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;

public class MiscResourceTest extends GaoshinResourceTester {
    @Test
	public void testMailboxWebService() throws JsonParseException, JsonMappingException, IOException {
        String request = getCurrentTimeMillisString();
        String resp = getBuilder("/misc/echo").post(String.class, request);
        Assert.assertEquals(request, resp);
	}
}
