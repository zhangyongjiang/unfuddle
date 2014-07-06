package com.gaoshin.appbooster.dao;

import java.util.List;

import com.gaoshin.appbooster.bean.City;

import common.geo.Geocode;

public interface GeoDao extends GenericDao {
    List<String> nearbyZipcodes(Float lat, Float lng, float radius);
    List<City> nearbyCities(Float lat, Float lng, float radius);
    Geocode getZipcodeLocation(String zipcode);
    List<City> listCitysInState(String string);
}
