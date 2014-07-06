package com.gaoshin.coupon.bean;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.coupon.entity.Coupon;
import com.gaoshin.coupon.entity.Store;

@XmlRootElement
public class StoreAndCoupon extends Coupon {
    private Store store;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
