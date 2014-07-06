package com.gaoshin.appbooster.webservice;

import junit.framework.Assert;

import org.junit.Test;

import com.gaoshin.appbooster.bean.UserDetails;
import com.gaoshin.appbooster.entity.Application;
import com.gaoshin.appbooster.entity.ApplicationType;


public class ApplicationTest extends PubliserResourceTest {
    @Test
    public void testAddApplication() {
        addApplication();
    }
    
    public Application addApplication() {
        Application app = new Application();
        String name = getCurrentTimeMillisString();
        app.setName(name);
        app.setIcon(getCurrentTimeMillisString());
        app.setDescription(getCurrentTimeMillisString());
        app.setType(ApplicationType.Android);
        app.setMarketId(getCurrentTimeMillisString());
        Application created = getBuilder("/ws/user/application/add").post(Application.class, app);
        app.setId(created.getId());
        
        UserDetails details = getBuilder("/ws/user/details").get(UserDetails.class);
        Assert.assertEquals(1, details.getApplicationDetailsList().getItems().size());
        Assert.assertEquals(name, details.getApplicationDetailsList().getItems().get(0).getName());
        Assert.assertEquals(ApplicationType.Android, details.getApplicationDetailsList().getItems().get(0).getType());
        Assert.assertEquals(app.getMarketId(), details.getApplicationDetailsList().getItems().get(0).getMarketId());
        
        return app;
    }
}
