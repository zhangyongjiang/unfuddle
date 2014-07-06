package com.gaoshin.onsalelocal.osl.entity;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.onsalelocal.api.Store;

@XmlRootElement
public class StoreDetails extends Store {
	private int offers;
	private int followers;

	public int getOffers() {
	    return offers;
    }

	public void setOffers(int offers) {
	    this.offers = offers;
    }

	public int getFollowers() {
	    return followers;
    }

	public void setFollowers(int followers) {
	    this.followers = followers;
    }

}
