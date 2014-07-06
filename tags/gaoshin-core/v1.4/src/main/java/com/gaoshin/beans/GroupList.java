package com.gaoshin.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GroupList {
    private List<Group> list = new ArrayList<Group>();

    public void setList(List<Group> list) {
        this.list = list;
    }

    public List<Group> getList() {
        return list;
    }
}
