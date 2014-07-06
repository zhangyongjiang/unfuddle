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
    
    public void setDefValue(String name, String defValue) {
        for(PluginParameter pp : items) {
            if(pp.getName().equals(name)) {
                pp.setDefValue(defValue);
            }
        }
    }
}
