package com.gaoshin.stock;

import java.util.ArrayList;
import java.util.List;

public class MenuList {
    private Integer group;
    private List<GaoshinMenuItem> items = new ArrayList<GaoshinMenuItem>();

    public List<GaoshinMenuItem> getItems() {
        return items;
    }

    public void setItems(List<GaoshinMenuItem> items) {
        this.items = items;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }
}
