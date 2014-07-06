package com.gaoshin.appbooster.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.appbooster.entity.Category;

@XmlRootElement
public class CategoryList {
    private List<Category> items = new ArrayList<Category>();

    public List<Category> getItems() {
        return items;
    }

    public void setItems(List<Category> items) {
        this.items = items;
    }

}
