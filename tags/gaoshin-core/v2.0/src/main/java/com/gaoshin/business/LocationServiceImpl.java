package com.gaoshin.business;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.beans.Location;
import com.gaoshin.beans.LocationList;
import com.gaoshin.beans.User;
import com.gaoshin.dao.LocationDao;
import com.gaoshin.dao.UserDao;
import com.gaoshin.entity.DeviceLocationEntity;
import com.gaoshin.entity.UserEntity;
import com.gaoshin.entity.UserLocationEntity;
import common.util.GeoUtil;
import common.web.BusinessException;
import common.web.ServiceError;

@Service("locationService")
@Transactional
public class LocationServiceImpl extends BaseServiceImpl implements LocationService {
    @Autowired
    private LocationDao      locationDao;

    @Autowired
    private UserDao userDao;

    @Override
    public void init() {
        locationDao.init();
    }

	@Override
    public Location save(User user, Location bean) {

        UserEntity userEntity = userDao.getUser(user);
        if (userEntity == null) {
            userEntity = new UserEntity(user);
            if (userEntity.getName() == null)
                userEntity.setName(user.getPhone());
            userDao.saveEntity(userEntity);
        }

        UserLocationEntity entity = null;
        if (bean.getId() != null) {
            entity = userDao.getEntity(UserLocationEntity.class, bean.getId());
            if (entity == null)
                throw new BusinessException(ServiceError.NotFound);
            if (!entity.getUserEntity().getId().equals(userEntity.getId()))
                throw new BusinessException(ServiceError.Forbidden);
            entity.copyFrom(bean);
            entity.resetLastUpdate();
            locationDao.saveEntity(entity);
        } else if (!bean.isDevice()) {
            List<UserLocationEntity> userLocations = locationDao.getUserLocations(userEntity.getId());
            if (userLocations.size() > 10)
                throw new BusinessException(ServiceError.InvalidInput, "too many user locations. max is 10");
            entity = new UserLocationEntity(bean);
            entity.resetLastUpdate();
            entity.setUserEntity(userEntity);
            locationDao.saveEntity(entity);
        }

        if (bean.isDevice()) {
            entity = updateUserDeviceLocation(userEntity, bean);
        }

        return entity.getBean(Location.class);
	}

    private UserLocationEntity updateUserDeviceLocation(UserEntity userEntity, Location bean) {
        UserLocationEntity userDeviceLocation = null;
        List<UserLocationEntity> userLocations = locationDao.getUserLocations(userEntity.getId());
        for (UserLocationEntity ule : userLocations) {
            if (ule.isDevice()) {
                userDeviceLocation = ule;
            }
        }

        if (userDeviceLocation == null) {
            userDeviceLocation = new UserLocationEntity(bean);
            userDeviceLocation.resetLastUpdate();
            userDeviceLocation.setUserEntity(userEntity);
            userDao.saveEntity(userDeviceLocation);

            // if this is the first time we get a device location, set it as
            // current location
            locationDao.setUserCurrentLocation(userDeviceLocation);

            DeviceLocationEntity lastLocation = new DeviceLocationEntity(bean);
            lastLocation.setFirstUpdate(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
            lastLocation.setLastUpdate(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
            lastLocation.setUserEntity(userEntity);
            userDao.saveEntity(lastLocation);
        } else {
            double distance = GeoUtil.distance(userDeviceLocation.getLatitude(), userDeviceLocation.getLongitude(),
                    bean.getLatitude(), bean.getLongitude(), 'K');
            if (distance < 0.01) {
                userDeviceLocation.setLastUpdate(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
                locationDao.saveEntity(userDeviceLocation);

                DeviceLocationEntity entity = locationDao.getUserLastDeviceLocation(userEntity.getId());
                if (entity != null) {
                    entity.resetLastUpdate();
                    locationDao.saveEntity(entity);
                } else {
                    entity = new DeviceLocationEntity(bean);
                    entity.setFirstUpdate(Calendar.getInstance(TimeZone.getTimeZone("UT")));
                    entity.resetLastUpdate();
                    entity.setUserEntity(userEntity);
                    userDao.saveEntity(entity);
                }
            } else {
                userDeviceLocation.copyFrom(bean);
                userDeviceLocation.resetLastUpdate();
                userDao.saveEntity(userDeviceLocation);

                DeviceLocationEntity entity = new DeviceLocationEntity(bean);
                entity.setFirstUpdate(Calendar.getInstance(TimeZone.getTimeZone("UT")));
                entity.resetLastUpdate();
                entity.setUserEntity(userEntity);
                userDao.saveEntity(entity);
            }
        }

        if (userEntity.getCurrentLocation() == null) {
            userEntity.setCurrentLocation(userDeviceLocation);
            userDao.saveEntity(userEntity);
        }
        return userDeviceLocation;
    }

    @Override
    public Location get(Long id) {
        UserLocationEntity entity = locationDao.getEntity(UserLocationEntity.class, id);
        Location location = entity.getBean(Location.class);
        User user = new User();
        user.setId(entity.getUserEntity().getId());
        return location;
	}

    @Override
    public LocationList getUserLocationList(User user) {
        UserEntity userEntity = userDao.getUser(user);
        LocationList list = new LocationList();
        List<UserLocationEntity> entities = locationDao.getUserLocations(userEntity.getId());
        for (UserLocationEntity entity : entities) {
            Location bean = entity.getBean(Location.class);
            if (userEntity.getCurrentLocation() != null && entity.getId().equals(userEntity.getCurrentLocation().getId())) {
                bean.setCurrent(true);
            }
            list.getList().add(bean);
        }
        return list;
    }

    @Override
    public LocationList getUserDeviceLocationList(User user, int offset, int size) {
        UserEntity userEntity = userDao.getUser(user);
        LocationList list = new LocationList();
        List<DeviceLocationEntity> entities = locationDao.findEntityBy(DeviceLocationEntity.class, "userEntity.id", userEntity.getId(), offset, size);
        for (DeviceLocationEntity entity : entities) {
            list.getList().add(entity.getBean(Location.class));
        }
        return list;
    }

    @Override
    public void setUserCurrentLocation(User user, long locationId) {
        UserEntity userEntity = userDao.getUser(user);
        UserLocationEntity entity = locationDao.getEntity(UserLocationEntity.class, locationId);
        if (entity == null) {
            throw new BusinessException(ServiceError.InvalidInput);
        }
        if (entity.getUserEntity().getId() != userEntity.getId()) {
            throw new BusinessException(ServiceError.Forbidden);
        }
        locationDao.setUserCurrentLocation(entity);
    }

    @Override
    public Location getUserCurrentLocation(User user) {
        UserEntity entity = userDao.getUser(user);
        return get(entity.getCurrentLocation().getId());
    }

}
