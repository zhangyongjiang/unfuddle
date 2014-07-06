package com.gaoshin.webservice;

import com.gaoshin.beans.GenericResponse;
import com.gaoshin.beans.Location;
import com.gaoshin.beans.User;
import common.web.ResourceTester;

public class GaoshinTester extends ResourceTester {
    protected static final float Latitude = 37.38536f;
    protected static final float Longitude = -122.09139f;

    protected User createUser() {
        User user = new User();
        Long irandom = System.currentTimeMillis();
        String srandom = irandom + "s";
        user.setPhone(srandom + "p");
        user.setName(srandom + "n");
        user.setPassword("password");
        User created = getBuilder("/user/signup").post(User.class, user);
        System.out.println("user created with id " + created.getId());
        return created;
    }

    protected void login(User user) {
        getBuilder("/user/login").post(User.class, user);
    }

    protected void logout() {
        getBuilder("/user/logout").post(GenericResponse.class, " ");
    }

    protected Location createLocation() {
        return createLocation(Latitude, Longitude, true);
    }

    protected Location createLocation(float latitude, float longitude, boolean isDevice) {
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setDevice(isDevice);
        location.setCity("city " + latitude);
        location.setState("state " + longitude);
        return getBuilder("/location/new").post(Location.class, location);
    }

}
