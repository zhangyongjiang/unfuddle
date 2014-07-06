package com.gaoshin.business;

import com.gaoshin.beans.Location;
import com.gaoshin.beans.LocationList;
import com.gaoshin.beans.User;

public interface LocationService {
    Location save(User user, Location bean);

    Location get(Long id);

    void init();

    LocationList getUserLocationList(User user);

    LocationList getUserDeviceLocationList(User user, int offset, int size);

    void setUserCurrentLocation(User user, long locationId);

    Location getUserCurrentLocation(User user);
}
