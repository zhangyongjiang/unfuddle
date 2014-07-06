package com.gaoshin.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CategoryList {
    private List<Category> list = new ArrayList<Category>();

    public void setList(List<Category> list) {
        this.list = list;
    }

    public List<Category> getList() {
        return list;
    }
}
