package com.gaoshin.onsalelocal.osl.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FollowDetails extends Follow {
	private User user;
	private User follower;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getFollower() {
		return follower;
	}

	public void setFollower(User follower) {
		this.follower = follower;
	}
}
