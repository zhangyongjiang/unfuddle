package com.gaoshin.cloud.web.xen.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VirtualNetworkInterfaceList {
    private List<VirtualNetworkInterface> items = new ArrayList<VirtualNetworkInterface>();

    public List<VirtualNetworkInterface> getItems() {
        return items;
    }

    public void setItems(List<VirtualNetworkInterface> items) {
        this.items = items;
    }
}
