package com.gaoshin.cloud.web.vm.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClusterList {
    private List<Cluster> items = new ArrayList<Cluster>();

    public List<Cluster> getItems() {
        return items;
    }

    public void setItems(List<Cluster> items) {
        this.items = items;
    }
}
