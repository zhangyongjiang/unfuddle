package com.gaoshin.coupon.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StoreAndCouponList {
    private List<StoreAndCoupon> items = new ArrayList<StoreAndCoupon>();

    public List<StoreAndCoupon> getItems() {
        return items;
    }

    public void setItems(List<StoreAndCoupon> items) {
        this.items = items;
    }
    
}
