package com.gaoshin.coupon.dao;

import java.util.List;

import com.gaoshin.coupon.bean.City;
import common.geo.Geocode;

public interface GeoDao extends GenericDao {
    List<String> nearbyZipcodes(Float lat, Float lng, float radius);
    List<City> nearbyCities(Float lat, Float lng, float radius);
    Geocode getZipcodeLocation(String zipcode);
    List<City> listCitysInState(String string);
}
