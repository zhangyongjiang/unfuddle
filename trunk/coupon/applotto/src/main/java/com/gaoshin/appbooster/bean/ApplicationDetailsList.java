package com.gaoshin.appbooster.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApplicationDetailsList {
    private List<ApplicationDetails> items = new ArrayList<ApplicationDetails>();

    public List<ApplicationDetails> getItems() {
        return items;
    }

    public void setItems(List<ApplicationDetails> items) {
        this.items = items;
    }
}
