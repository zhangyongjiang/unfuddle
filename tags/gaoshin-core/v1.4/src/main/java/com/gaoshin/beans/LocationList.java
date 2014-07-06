package com.gaoshin.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LocationList {
    private List<Location> list = new ArrayList<Location>();

    public void setList(List<Location> list) {
        this.list = list;
    }

    public List<Location> getList() {
        return list;
    }
}
