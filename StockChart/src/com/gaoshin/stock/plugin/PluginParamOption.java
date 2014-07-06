package com.gaoshin.stock.plugin;

public class PluginParamOption {
    private String display;
    private String value;

    public PluginParamOption() {
    }
    
    public PluginParamOption(String display, String value) {
        this.display = display;
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
