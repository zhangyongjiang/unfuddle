package com.gaoshin.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DimensionValueList {
    private List<DimensionValue> list = new ArrayList<DimensionValue>();

    public void setList(List<DimensionValue> list) {
        this.list = list;
    }

    public List<DimensionValue> getList() {
        return list;
    }

}
