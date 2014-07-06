package com.gaoshin.dao.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gaoshin.dao.LocationDao;
import com.gaoshin.entity.DeviceLocationEntity;
import com.gaoshin.entity.UserLocationEntity;

@Repository("locationDao")
public class LocationDaoJpaImpl extends DaoComponentImpl implements LocationDao {
    @Override
    public void init() {
        super.init();

        if (isMysql()) {
            createMysqlIndex();
        }
    }

    private void createMysqlIndex() {
        String sql = null;

        {
            sql = "SHOW INDEX FROM user_location WHERE Key_name = 'user_location_search_index'";
            List<Object[]> index = nativeQuerySelect(sql);
            if (index == null || index.size() == 0) {
                sql = "alter table user_location add index user_location_search_index (latitude, longitude)";
                nativeQueryUpdate(sql);
            }

            sql = "SHOW COLUMNS FROM user_location WHERE field = 'latitude'";
            Object[] columns = (Object[]) nativeQuerySelectRow(sql);
            if ("float".equalsIgnoreCase(columns[1].toString())) {
                sql = "alter table user_location change latitude latitude float(9,5) not null";
                nativeQueryUpdate(sql);
            }

            sql = "SHOW COLUMNS FROM user_location WHERE field = 'longitude'";
            columns = (Object[]) nativeQuerySelectRow(sql);
            if ("float".equalsIgnoreCase(columns[1].toString())) {
                sql = "alter table user_location change longitude longitude float(9,5) not null";
                nativeQueryUpdate(sql);
            }
        }

        {
            sql = "SHOW INDEX FROM DEVICE_LOC WHERE Key_name = 'device_location_search_index'";
            List<Object[]> index = nativeQuerySelect(sql);
            if (index == null || index.size() == 0) {
                sql = "alter table DEVICE_LOC add index device_location_search_index (latitude, longitude)";
                nativeQueryUpdate(sql);
            }

            sql = "SHOW COLUMNS FROM DEVICE_LOC WHERE field = 'latitude'";
            Object[] columns = (Object[]) nativeQuerySelectRow(sql);
            if ("float".equalsIgnoreCase(columns[1].toString())) {
                sql = "alter table DEVICE_LOC change latitude latitude float(9,5) not null";
                nativeQueryUpdate(sql);
            }

            sql = "SHOW COLUMNS FROM DEVICE_LOC WHERE field = 'longitude'";
            columns = (Object[]) nativeQuerySelectRow(sql);
            if ("float".equalsIgnoreCase(columns[1].toString())) {
                sql = "alter table DEVICE_LOC change longitude longitude float(9,5) not null";
                nativeQueryUpdate(sql);
            }
        }
    }

    @Override
    public List<UserLocationEntity> getUserLocations(Long id) {
        List<UserLocationEntity> userLocations = findEntityBy(UserLocationEntity.class, "userEntity.id", id);
        return userLocations;
    }

    @Override
    public DeviceLocationEntity getUserLastDeviceLocation(Long userId) {
        List<DeviceLocationEntity> list = findEntityBy(DeviceLocationEntity.class, "userEntity.id", userId, 0, 1, "lastUpdate desc");
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    public void setUserCurrentLocation(UserLocationEntity entity) {
        entity.getUserEntity().setCurrentLocation(entity);
        saveEntity(entity.getUserEntity());
    }
}
