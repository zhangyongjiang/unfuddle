package com.gaoshin.onsalelocal.osl.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NotificationList {
	private List<Notification> items = new ArrayList<Notification>();

	public List<Notification> getItems() {
	    return items;
    }

	public void setItems(List<Notification> items) {
	    this.items = items;
    }

}
