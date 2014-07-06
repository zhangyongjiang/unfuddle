package com.gaoshin.coupon.crawler;

import java.util.ArrayList;
import java.util.List;


public class WeeklyAd {
    private String validDate;
    private static List<CouponCategory> categories = new ArrayList<CouponCategory>();

    public static List<CouponCategory> getCategories() {
        return categories;
    }

    public static void setCategories(List<CouponCategory> categories) {
        WeeklyAd.categories = categories;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }
}
