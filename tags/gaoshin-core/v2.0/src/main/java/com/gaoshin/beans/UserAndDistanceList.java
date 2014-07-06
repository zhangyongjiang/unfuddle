package com.gaoshin.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserAndDistanceList {
    private int miles;
    private List<UserAndDistance> list = new ArrayList<UserAndDistance>();

    public void setList(List<UserAndDistance> list) {
        this.list = list;
    }

    public List<UserAndDistance> getList() {
        return list;
    }

    public void setMiles(int miles) {
        this.miles = miles;
    }

    public int getMiles() {
        return miles;
    }
}
