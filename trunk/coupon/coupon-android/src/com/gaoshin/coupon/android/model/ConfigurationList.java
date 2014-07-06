package com.gaoshin.coupon.android.model;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationList {
    private List<Configuration> list = new ArrayList<Configuration>();

    public void setList(List<Configuration> list) {
        this.list = list;
    }

    public List<Configuration> getList() {
        return list;
    }
}
