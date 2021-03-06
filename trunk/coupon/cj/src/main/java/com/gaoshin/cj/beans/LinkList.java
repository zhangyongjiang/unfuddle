package com.gaoshin.cj.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.cj.entity.Link;

@XmlRootElement
public class LinkList {
    private List<Link> items = new ArrayList<Link>();

    public List<Link> getItems() {
        return items;
    }

    public void setItems(List<Link> items) {
        this.items = items;
    }
}
