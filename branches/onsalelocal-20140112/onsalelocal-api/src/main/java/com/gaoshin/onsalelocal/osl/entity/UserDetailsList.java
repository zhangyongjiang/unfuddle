package com.gaoshin.onsalelocal.osl.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDetailsList  {
	private List<UserDetails> items = new ArrayList<UserDetails>();

	public List<UserDetails> getItems() {
	    return items;
    }

	public void setItems(List<UserDetails> items) {
	    this.items = items;
    }
}
