package com.gaoshin.stock;

import java.util.ArrayList;
import java.util.List;

public class MenuGroupList {
    private Integer pluginId;
    private List<MenuList> items = new ArrayList<MenuList>();

    public List<MenuList> getItems() {
        return items;
    }

    public void setItems(List<MenuList> items) {
        this.items = items;
    }

    public Integer getPluginId() {
        return pluginId;
    }

    public void setPluginId(Integer pluginId) {
        this.pluginId = pluginId;
    }
}
