package com.gaoshin.onsalelocal.osl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import common.db.entity.DbEntity;

@Entity
@Table
@XmlRootElement
public class Follow extends DbEntity {
	@Column(nullable = true, length = 64)
	private String userId;

	@Column(nullable = true, length = 64)
	private String followerId;

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFollowerId() {
	    return followerId;
    }

	public void setFollowerId(String followerId) {
	    this.followerId = followerId;
    }

}
