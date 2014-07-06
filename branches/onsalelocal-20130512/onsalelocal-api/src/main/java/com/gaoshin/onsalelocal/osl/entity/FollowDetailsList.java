package com.gaoshin.onsalelocal.osl.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FollowDetailsList {
	private User user;
	private List<FollowDetails> items = new ArrayList<FollowDetails>();

	public List<FollowDetails> getItems() {
		return items;
	}

	public void setItems(List<FollowDetails> items) {
		this.items = items;
	}

	public User getUser() {
	    return user;
    }

	public void setUser(User user) {
	    this.user = user;
    }
}
