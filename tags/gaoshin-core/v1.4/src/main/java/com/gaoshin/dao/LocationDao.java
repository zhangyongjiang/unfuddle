package com.gaoshin.dao;

import java.util.List;

import com.gaoshin.dao.jpa.DaoComponent;
import com.gaoshin.entity.DeviceLocationEntity;
import com.gaoshin.entity.UserLocationEntity;

public interface LocationDao extends DaoComponent {

    List<UserLocationEntity> getUserLocations(Long id);

    DeviceLocationEntity getUserLastDeviceLocation(Long id);

    void setUserCurrentLocation(UserLocationEntity entity);

}
