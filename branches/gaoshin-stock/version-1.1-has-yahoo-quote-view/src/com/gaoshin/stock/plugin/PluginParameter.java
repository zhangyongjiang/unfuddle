package com.gaoshin.stock.plugin;

import java.util.ArrayList;
import java.util.List;

public class PluginParameter {
    private String name;
    private String display;
    private List<PluginParamOption> options = new ArrayList<PluginParamOption>();
    private String defValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getDefValue() {
        return defValue;
    }

    public void setDefValue(String defValue) {
        this.defValue = defValue;
    }

    public List<PluginParamOption> getOptions() {
        return options;
    }

    public void setOptions(List<PluginParamOption> options) {
        this.options = options;
    }

}
