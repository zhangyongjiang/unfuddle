package com.gaoshin.onsalelocal.osl.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDetails extends User {
	private int followers;
	private int followings;
	private int offers;

	public int getFollowers() {
		return followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	public int getFollowings() {
		return followings;
	}

	public void setFollowings(int followings) {
		this.followings = followings;
	}

	public int getOffers() {
	    return offers;
    }

	public void setOffers(int offers) {
	    this.offers = offers;
    }
}
