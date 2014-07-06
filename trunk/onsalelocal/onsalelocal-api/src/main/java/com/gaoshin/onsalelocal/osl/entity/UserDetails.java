package com.gaoshin.onsalelocal.osl.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDetails extends User {
	private int followers;
	private int followings;
	private int offers;
	private int likes;
	private int stores;
	private boolean myFollower;
	private boolean myFollowing;
	private int notifications;
	private String fbid;

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

	public boolean isMyFollower() {
	    return myFollower;
    }

	public void setMyFollower(boolean myFollower) {
	    this.myFollower = myFollower;
    }

	public boolean isMyFollowing() {
	    return myFollowing;
    }

	public void setMyFollowing(boolean myFollowing) {
	    this.myFollowing = myFollowing;
    }

	public int getLikes() {
	    return likes;
    }

	public void setLikes(int likes) {
	    this.likes = likes;
    }

	public int getStores() {
	    return stores;
    }

	public void setStores(int stores) {
	    this.stores = stores;
    }

	public int getNotifications() {
	    return notifications;
    }

	public void setNotifications(int notifications) {
	    this.notifications = notifications;
    }

	public String getFbid() {
	    return fbid;
    }

	public void setFbid(String fbid) {
	    this.fbid = fbid;
    }
}
