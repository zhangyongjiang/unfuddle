package com.gaoshin.stock.plugin;

import com.gaoshin.sorma.annotation.Table;

@Table(
        keyColumn = "id",
        autoId = true,
        create = {
                "create table Plugin (" +
                        "id INTEGER primary key autoincrement " +
                        ", name text " +
                        ", enabled integer " +
                        ", country text " +
                        ", description text " +
                        ", url text " +
                        ", postAction text " +
                        ", paramsJson text " +
                        ", sequence bigint " +
                        ", sampleUrl text " +
                        ", type integer " +
                        ")"
        })
public class Plugin {
    private Integer id;
    private String country;
    private String name;
    private String description;
    private String url;
    private String postAction;
    private String paramsJson;
    private boolean enabled;
    private long sequence;
    private String sampleUrl;
    private int type;
    private PluginParamList paramList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPostAction() {
        return postAction;
    }

    public void setPostAction(String postAction) {
        this.postAction = postAction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParamsJson() {
        return paramsJson;
    }

    public void setParamsJson(String paramsJson) {
        this.paramsJson = paramsJson;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PluginParamList getParamList() {
        return paramList;
    }

    public void setParamList(PluginParamList paramList) {
        this.paramList = paramList;
    }

    public String getSampleUrl() {
        return sampleUrl;
    }

    public void setSampleUrl(String sampleUrl) {
        this.sampleUrl = sampleUrl;
    }
}
