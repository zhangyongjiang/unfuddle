package com.gaoshin.cloud.web.job.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RuntimeConfList {
    private List<RuntimeJobConf> items = new ArrayList<RuntimeJobConf>();

    public List<RuntimeJobConf> getItems() {
        return items;
    }

    public void setItems(List<RuntimeJobConf> items) {
        this.items = items;
    }
}
