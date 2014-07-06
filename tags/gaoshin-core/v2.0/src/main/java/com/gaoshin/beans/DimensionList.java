package com.gaoshin.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DimensionList {
    private List<Dimension> list = new ArrayList<Dimension>();

    public void setList(List<Dimension> list) {
        this.list = list;
    }

    public List<Dimension> getList() {
        return list;
    }
}
