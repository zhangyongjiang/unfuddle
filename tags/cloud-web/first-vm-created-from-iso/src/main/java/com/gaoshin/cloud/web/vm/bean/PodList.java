package com.gaoshin.cloud.web.vm.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PodList {
    private List<Pod> items = new ArrayList<Pod>();

    public List<Pod> getItems() {
        return items;
    }

    public void setItems(List<Pod> items) {
        this.items = items;
    }
}
