package com.gaoshin.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ObjectBeanList {
    private List<ObjectBean> list = new ArrayList<ObjectBean>();

    public void setList(List<ObjectBean> list) {
        this.list = list;
    }

    public List<ObjectBean> getList() {
        return list;
    }
}
