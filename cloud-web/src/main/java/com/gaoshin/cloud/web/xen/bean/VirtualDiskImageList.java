package com.gaoshin.cloud.web.xen.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VirtualDiskImageList {
    private List<VirtualDiskImage> items = new ArrayList<VirtualDiskImage>();

    public List<VirtualDiskImage> getItems() {
        return items;
    }

    public void setItems(List<VirtualDiskImage> items) {
        this.items = items;
    }
}
