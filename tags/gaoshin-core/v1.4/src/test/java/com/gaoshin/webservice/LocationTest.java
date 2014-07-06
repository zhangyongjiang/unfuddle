package com.gaoshin.webservice;

import org.junit.Assert;
import org.junit.Test;

import com.gaoshin.beans.Location;
import com.gaoshin.beans.LocationList;
import com.gaoshin.beans.User;
import com.sun.jersey.api.client.UniformInterfaceException;
import common.web.ServiceError;

public class LocationTest extends GaoshinTester {
    @Test
    public void testLocationResource() {
        try {
            createLocation();
        } catch (UniformInterfaceException e) {
            Assert.assertEquals(ServiceError.NoGuest.getErrorCode(), e.getResponse().getStatus());
        }

        User user = createUser();
        login(user);

        Location created = createLocation(Latitude, Longitude, false);
        Location resp = getBuilder("/location/" + created.getId()).get(Location.class);
        Assert.assertNotNull(resp.getId());

        LocationList list = getBuilder("/location/my").get(LocationList.class);
        Assert.assertEquals(1, list.getList().size());

        Location loc2 = createLocation(Latitude + 0.1f, Longitude + 0.1f, false);

        LocationList list2 = getBuilder("/location/my").get(LocationList.class);
        Assert.assertEquals(2, list2.getList().size());

        logout();
    }

    @Test
    public void testDeviceLocation() {
        User user = createUser();
        login(user);

        createLocation(Latitude, Longitude, true);
        LocationList list = getBuilder("/location/my").get(LocationList.class);
        Assert.assertEquals(1, list.getList().size());

        LocationList list1 = getBuilder("/location/device").get(LocationList.class);
        Assert.assertEquals(1, list1.getList().size());

        createLocation(Latitude, Longitude, true);
        LocationList list2 = getBuilder("/location/device").get(LocationList.class);
        Assert.assertEquals(1, list2.getList().size());

        createLocation(Latitude + 0.1f, Longitude + 0.1f, true);
        LocationList list3 = getBuilder("/location/device").get(LocationList.class);
        Assert.assertEquals(2, list3.getList().size());

        LocationList list4 = getBuilder("/location/my").get(LocationList.class);
        Assert.assertEquals(1, list4.getList().size());

        logout();
    }

}
