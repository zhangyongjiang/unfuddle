package com.gaoshin.appbooster.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.appbooster.entity.Application;

@XmlRootElement
public class ApplicationList {
    private List<Application> items = new ArrayList<Application>();

    public List<Application> getItems() {
        return items;
    }

    public void setItems(List<Application> items) {
        this.items = items;
    }
}
