package com.gaoshin.coupon.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.coupon.entity.Coupon;

@XmlRootElement
public class CouponList {
    private List<Coupon> items = new ArrayList<Coupon>();

    public List<Coupon> getItems() {
        return items;
    }

    public void setItems(List<Coupon> items) {
        this.items = items;
    }
}
