package com.gaoshin.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NotificationList {
    private List<Notification> list = new ArrayList<Notification>();

    public void setList(List<Notification> list) {
        this.list = list;
    }

    public List<Notification> getList() {
        return list;
    }
}
