package com.gaoshin.stock.plugin;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class UrlParameter {
    private String name;
    private String value;
    private String description;
    private List<ParameterValue> options = new ArrayList<ParameterValue>();

    public UrlParameter() {
    }
    
    public UrlParameter(String kv) {
        String[] keyValue = kv.split("=");
        name = keyValue[0];
        if(keyValue.length > 1) {
            value = URLDecoder.decode(keyValue[1]);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ParameterValue> getOptions() {
        return options;
    }

    public void setOptions(List<ParameterValue> values) {
        this.options = values;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if(value != null) {
            return name + "=" + URLEncoder.encode(value);
        }
        else {
            return name + "=";
        }
    }
}
