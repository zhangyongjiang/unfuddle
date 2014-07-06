package com.gaoshin.coupon.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.coupon.entity.Store;

@XmlRootElement
public class StoreList {
    private List<Store> items = new ArrayList<Store>();

    public List<Store> getItems() {
        return items;
    }

    public void setItems(List<Store> items) {
        this.items = items;
    }
}
