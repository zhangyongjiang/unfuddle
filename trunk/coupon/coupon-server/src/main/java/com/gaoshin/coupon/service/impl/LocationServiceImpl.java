package com.gaoshin.coupon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.coupon.bean.City;
import com.gaoshin.coupon.dao.GeoDao;
import com.gaoshin.coupon.service.LocationService;

@Service
@Transactional(readOnly=true)
public class LocationServiceImpl extends ServiceBaseImpl implements LocationService {
    @Autowired private GeoDao geoDao;

    @Override
    public List<City> listNearbyCities(float latitude, float longitude, float radius) {
        return geoDao.nearbyCities(latitude, longitude, radius);
    }

}
