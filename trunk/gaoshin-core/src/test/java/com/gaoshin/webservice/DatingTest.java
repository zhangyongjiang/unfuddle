package com.gaoshin.webservice;

import org.junit.Test;

import com.gaoshin.beans.LocationDistance;
import com.gaoshin.beans.User;
import com.gaoshin.beans.UserAndDistance;
import com.gaoshin.beans.UserAndDistanceList;
import common.util.GeoUtil;
import common.util.GeoUtil.GeoRange;

public class DatingTest extends GaoshinTester {
    @Test
    public void testSearch() {
        User user = createUser();
        login(user);
        GeoRange range5 = GeoUtil.getRange(Latitude, Longitude, 5);
        createLocation(range5.getMinLat(), range5.getMinLng(), true);
        logout();

        User user2 = createUser();
        login(user2);
        GeoRange range7 = GeoUtil.getRange(Latitude, Longitude, 7);
        createLocation(range7.getMinLat(), range7.getMinLng(), true);
        logout();

        User user3 = createUser();
        login(user3);
        GeoRange range9 = GeoUtil.getRange(Latitude, Longitude, 9);
        createLocation(range9.getMinLat(), range9.getMinLng(), true);

        for (int i = 1; i < 10; i++) {
            UserAndDistanceList userList = getBuilder("/user/search", "lat", Latitude, "lng", Longitude, "miles", i).get(UserAndDistanceList.class);
            System.out.println("mile: " + i + ", result # is " + userList.getList().size());
            for (UserAndDistance u : userList.getList()) {
                LocationDistance ld = u.getLocationDistance();
                System.out.println("user id: " + u.getId()
                        + ", user name: " + u.getName()
                        + ", user phone: " + user.getPhone()
                        + ", distance : " + ld.getDistance()
                        + ", city : " + ld.getCity()
                        + ", state : " + ld.getState()
                        );
            }
        }
    }
}
