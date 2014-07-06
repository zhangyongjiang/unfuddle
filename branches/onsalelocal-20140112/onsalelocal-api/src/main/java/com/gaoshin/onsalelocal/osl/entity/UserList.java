package com.gaoshin.onsalelocal.osl.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserList  {
	private List<User> items = new ArrayList<User>();

	public List<User> getItems() {
	    return items;
    }

	public void setItems(List<User> items) {
	    this.items = items;
    }
}
