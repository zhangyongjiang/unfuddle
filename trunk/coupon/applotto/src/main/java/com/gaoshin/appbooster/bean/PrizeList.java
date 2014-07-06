package com.gaoshin.appbooster.bean;

import java.util.ArrayList;
import java.util.List;

import com.gaoshin.appbooster.entity.Prize;

public class PrizeList {
    private List<Prize> items = new ArrayList<Prize>();

    public List<Prize> getItems() {
        return items;
    }

    public void setItems(List<Prize> items) {
        this.items = items;
    }
}
