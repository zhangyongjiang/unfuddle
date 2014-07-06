package com.gaoshin.coupon.crawler;

import java.util.ArrayList;
import java.util.List;

public class CouponCategory {
    private String id;
    private String name;
    private String url;
    private int howmany;
    private List<WebCoupon> coupons = new ArrayList<WebCoupon>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHowmany() {
        return howmany;
    }

    public void setHowmany(int howmany) {
        this.howmany = howmany;
    }
    
    @Override
    public String toString() {
        return id + ", " + name + ", " + howmany + ", " + url; 
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<WebCoupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<WebCoupon> coupons) {
        this.coupons = coupons;
    }

}
