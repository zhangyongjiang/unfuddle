package com.gaoshin.amazon;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ItemInterestList {
    private List<ItemInterest> list = new ArrayList<ItemInterest>();

    public void setList(List<ItemInterest> list) {
        this.list = list;
    }

    public List<ItemInterest> getList() {
        return list;
    }
}
