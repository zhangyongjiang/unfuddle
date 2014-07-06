package com.gaoshin.amazon;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NodeInterestList {
    private List<NodeInterest> list = new ArrayList<NodeInterest>();

    public void setList(List<NodeInterest> list) {
        this.list = list;
    }

    public List<NodeInterest> getList() {
        return list;
    }
}
