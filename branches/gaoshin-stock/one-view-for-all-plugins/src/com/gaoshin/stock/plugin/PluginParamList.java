package com.gaoshin.stock.plugin;

import java.util.ArrayList;
import java.util.List;

public class PluginParamList {
    private List<PluginParameter> items = new ArrayList<PluginParameter>();

    public List<PluginParameter> getItems() {
        return items;
    }

    public void setItems(List<PluginParameter> items) {
        this.items = items;
    }
}
