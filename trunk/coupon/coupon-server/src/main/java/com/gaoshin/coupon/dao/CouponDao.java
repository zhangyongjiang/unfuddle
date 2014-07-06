package com.gaoshin.coupon.dao;

import java.util.List;

import com.gaoshin.coupon.entity.Coupon;
import com.gaoshin.coupon.entity.ShoplocalCrawlHistory;
import com.gaoshin.coupon.entity.ShoplocalResult;

public interface CouponDao extends GenericDao {
    void removeCoupon(String id);
    List<Coupon> searchCoupon(Float lat, Float lng, float radius, String category, String keywords, int offset, int size);
    ShoplocalCrawlHistory getOneShoplocalTask(String city, String state);
    ShoplocalCrawlHistory getOneShoplocalResult();
    List<ShoplocalCrawlHistory> listShoplocalHistory(String status, int offset, int size);
    ShoplocalResult getShoplocalResult(String id, int page);
    List<ShoplocalResult> getShoplocalResults(String taskid);
}
