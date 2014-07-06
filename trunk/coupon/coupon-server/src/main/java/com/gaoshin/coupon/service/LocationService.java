package com.gaoshin.coupon.service;

import java.util.List;

import com.gaoshin.coupon.bean.City;

public interface LocationService {

    List<City> listNearbyCities(float latitude, float longitude, float radius);

}
