package com.gaoshin.coupon.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.gaoshin.coupon.dao.CouponDao;
import com.gaoshin.coupon.entity.Coupon;
import com.gaoshin.coupon.entity.FetchStatus;
import com.gaoshin.coupon.entity.ShoplocalCrawlHistory;
import com.gaoshin.coupon.entity.ShoplocalResult;
import common.geo.GeoRange;
import common.geo.Geocode;

@Repository
public class CouponDaoImpl extends GenericDaoImpl implements CouponDao {

    @Override
    public void removeCoupon(String id) {
        delete(Coupon.class, Collections.singletonMap("id", id));
        delete(Coupon.class, Collections.singletonMap("parentId", id));
    }

    @Override
    public List<Coupon> searchCoupon(Float lat, Float lng, float radius, String category, String keywords, int offset, int size) {
        GeoRange range = Geocode.getRange(lat, lng, radius);
        String sql = "select *, ((ACOS(SIN(:lat * PI() / 180) * SIN(latitude * PI() / 180) + COS(:lat * PI() / 180) * COS(latitude * PI() / 180) * COS((:lng - longitude) * PI() / 180)) * 180 / PI()) * 60 * 1.1515) AS `distance` from Coupon " +
        		" where latitude>:lat0 and latitude<:lat1 " +
        		" and longitude>:lng0 and longitude<:lng1 " +
                (category == null ? "" : " and parentCategory=:parentCategory ") + 
                (keywords == null ? "" : " and match(title, description) against (:keywords) ") + 
        		" order by distance limit :offset, :size";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("lat0", range.getMinLat());
        params.put("lat1", range.getMaxLat());
        params.put("lng0", range.getMinLng());
        params.put("lng1", range.getMaxLng());
        params.put("offset", offset);
        params.put("size", size);
        if(category != null) {
            params.put("parentCategory", category);
        }
        if(keywords != null) {
            params.put("keywords", keywords);
        }
        
        return queryBySql(Coupon.class, params, sql);
    }

    @Override
    public ShoplocalCrawlHistory getOneShoplocalTask(String city, String state) {
        String uuid = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        if(city != null && state != null) {
            String sql = "update ShoplocalCrawlHistory set updated=" + now + ", updateId='" + uuid + "', status='Pending' where status is null and city=? and state=? limit 1";
            getJdbcTemplate().update(sql, city, state);
        }
        else {
            String sql = "update ShoplocalCrawlHistory set updated=" + now + ", updateId='" + uuid + "', status='Pending' where status is null limit 1";
            getJdbcTemplate().update(sql);
        }
        ShoplocalCrawlHistory history = getUniqueResult(ShoplocalCrawlHistory.class, Collections.singletonMap("updateId", uuid));
        return history;
    }

    @Override
    public ShoplocalCrawlHistory getOneShoplocalResult() {
        String uuid = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        String sql = "update ShoplocalCrawlHistory set updated=" + now + ", updateId='" + uuid + "', status='Processing' where status ='" + FetchStatus.HtmlReady + "' limit 1";
        getJdbcTemplate().update(sql);
        ShoplocalCrawlHistory history = getUniqueResult(ShoplocalCrawlHistory.class, Collections.singletonMap("updateId", uuid));
        return history;
    }

    @Override
    public List<ShoplocalCrawlHistory> listShoplocalHistory(String status, int offset, int size) {
        String sql = null;
        Object[] params = null;
        if(status != null) {
            sql = "select id,created,updated,city,state,status,url,category,fetched,total,updateId,page from ShoplocalCrawlHistory where status=? limit ?, ?";
            params = new Object[3];
            params[0] = status;
            params[1] = offset;
            params[2] = size;
        }
        else {
            sql = "select id,created,updated,city,state,status,url,category,fetched,total,updateId,page from ShoplocalCrawlHistory limit ?, ?";
            params = new Object[2];
            params[0] = offset;
            params[1] = size;
        }
        List<ShoplocalCrawlHistory> list = getJdbcTemplate().query(sql, params, new RowMapper<ShoplocalCrawlHistory>(){
            @Override
            public ShoplocalCrawlHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
                ShoplocalCrawlHistory rec = new ShoplocalCrawlHistory();
                rec.setId(rs.getString(1));
                rec.setCreated(rs.getLong(2));
                rec.setUpdated(rs.getLong(3));
                rec.setCity(rs.getString(4));
                rec.setState(rs.getString(5));
                rec.setStatus(FetchStatus.valueOf(rs.getString(6)));
                rec.setUrl(rs.getString(7));
                rec.setCategory(rs.getString(8));
                rec.setUpdateId(rs.getString(11));
                rec.setPage(rs.getInt(12));
                return rec;
            }});

        return list;
    }

    @Override
    public ShoplocalResult getShoplocalResult(final String taskId, final int page) {
        return getUniqueResult(ShoplocalResult.class, new HashMap<String, Object>(){{
            put("taskId", taskId);
            put("page", page);
        }});
    }

    @Override
    public List<ShoplocalResult> getShoplocalResults(String taskid) {
        return query(ShoplocalResult.class, Collections.singletonMap("taskId", taskid));
    }
}
