package com.gaoshin.cloud.web.xen.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VirtualBlockDeviceList {
    private List<VirtualBlockDevice> items = new ArrayList<VirtualBlockDevice>();

    public List<VirtualBlockDevice> getItems() {
        return items;
    }

    public void setItems(List<VirtualBlockDevice> items) {
        this.items = items;
    }
}
