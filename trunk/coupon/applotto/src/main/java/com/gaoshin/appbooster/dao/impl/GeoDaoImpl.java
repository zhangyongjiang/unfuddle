package com.gaoshin.appbooster.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.gaoshin.appbooster.bean.City;
import com.gaoshin.appbooster.dao.GeoDao;

import common.geo.GeoRange;
import common.geo.Geocode;

@Repository
public class GeoDaoImpl extends GenericDaoImpl implements GeoDao {

    @Override
    public List<String> nearbyZipcodes(Float lat, Float lng, float radius) {
        GeoRange range = Geocode.getRange(lat, lng, radius);
        String sql = "select ((ACOS(SIN(:lat * PI() / 180) * SIN(lat * PI() / 180) + COS(:lat * PI() / 180) * COS(lat * PI() / 180) * COS((:lng - lon) * PI() / 180)) * 180 / PI()) * 60 * 1.1515) AS `distance`, zip from geo.uszips where lat>:lat0 and lat<:lat1 and lon>:lng0 and lon<:lng1 order by distance";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("lat0", range.getMinLat());
        params.put("lat1", range.getMaxLat());
        params.put("lng0", range.getMinLng());
        params.put("lng1", range.getMaxLng());
        List<String> list = this.getNamedParameterJdbcTemplate().query(sql, params, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(2);
            }
        });
        return list;
    }

    @Override
    public List<City> nearbyCities(Float lat, Float lng, float radius) {
        GeoRange range = Geocode.getRange(lat, lng, radius);
        String sql = "select ((ACOS(SIN(:lat * PI() / 180) * SIN(lat * PI() / 180) + COS(:lat * PI() / 180) * COS(lat * PI() / 180) * COS((:lng - lon) * PI() / 180)) * 180 / PI()) * 60 * 1.1515) AS `distance`" +
        		", city, state from geo.uscities" +
        		" where lat>:lat0 and lat<:lat1 and lon>:lng0 and lon<:lng1" +
        		" order by distance";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("lat0", range.getMinLat());
        params.put("lat1", range.getMaxLat());
        params.put("lng0", range.getMinLng());
        params.put("lng1", range.getMaxLng());
        List<City> list = this.getNamedParameterJdbcTemplate().query(sql, params, new RowMapper<City>() {
            @Override
            public City mapRow(ResultSet rs, int rowNum) throws SQLException {
                City city = new City();
                city.setCity(rs.getString(2));
                city.setState(rs.getString(3));
                return city;
            }
        });
        return list;
    }

    @Override
    public Geocode getZipcodeLocation(String zipcode) {
        String sql = "select lat as latitude, lon as longitude from geo.uszips where zip=:zip";
        return queryUniqueBySql(Geocode.class, Collections.singletonMap("zip", zipcode), sql );
    }

    @Override
    public List<City> listCitysInState(String state) {
        String sql = "select * from geo.uscities where state=:state";
        return queryBySql(City.class, Collections.singletonMap("state", state), sql);
    }
}
