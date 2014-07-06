package com.gaoshin.coupon.service;

import java.util.List;

import com.gaoshin.coupon.bean.Client;
import com.gaoshin.coupon.bean.CouponList;
import com.gaoshin.coupon.bean.WebPage;
import com.gaoshin.coupon.entity.Coupon;
import com.gaoshin.coupon.entity.ShoplocalCrawlHistory;
import com.gaoshin.coupon.entity.ShoplocalResult;

public interface CouponService extends ServiceBase {
    void createCoupon(String userId, Coupon coupon);
    void enableCoupon(String userId, String id);
    void disableCoupon(String userId, String id);
    void removeCoupon(String userId, String id);
    CouponList crawl(Float lat, Float lng) throws Exception;
    CouponList crawl(String zipcode) throws Exception;
    CouponList searchByGeo(Float lat, Float lng, Float radius, String category, String keywords, int offset, int size) throws Exception;
    int crawlShoplocal() throws Exception;
    void stopShoplocal();
    Coupon getDetails(String couponId) throws Exception;
    ShoplocalCrawlHistory getOneShoplocalTask(Client client) throws Exception;
    void uploadShoplocalResult(WebPage page) throws Exception;
    int processShoplocalHtml() throws Exception;
    List<ShoplocalCrawlHistory> listShoplocalHistory(String status, int offset, int size);
    ShoplocalResult getShoplocalResult(String taskid, int page);
    List<ShoplocalResult> getShoplocalResults(String taskid);
    List<ShoplocalResult> listShoplocalResults(String taskid);
}
