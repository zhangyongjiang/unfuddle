package com.gaoshin.cloud.web.vm.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataCenterList {
    private List<DataCenter> items = new ArrayList<DataCenter>();

    public List<DataCenter> getItems() {
        return items;
    }

    public void setItems(List<DataCenter> items) {
        this.items = items;
    }
}
