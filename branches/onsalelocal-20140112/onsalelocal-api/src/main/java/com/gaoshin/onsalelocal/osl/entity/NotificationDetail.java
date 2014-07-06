package com.gaoshin.onsalelocal.osl.entity;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.Store;

@XmlRootElement
public class NotificationDetail extends Notification {
	private User user;
	private Store store;
	private Offer offer;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Offer getOffer() {
	    return offer;
    }

	public void setOffer(Offer offer) {
	    this.offer = offer;
    }
}
