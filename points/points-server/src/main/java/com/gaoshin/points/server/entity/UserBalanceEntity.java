package com.gaoshin.points.server.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class UserBalanceEntity implements Serializable {
    @Id @GeneratedValue(generator="SameUserShardIdGenerator")
    @Column(length=64) 
    @GenericGenerator(name="SameUserShardIdGenerator", strategy="com.gaoshin.points.server.shard.SameUserShardIdGenerator")
	private String id;
    
    @Column(nullable=false, length=64)
    private String userId;

    @Column(nullable=false, length=64)
    private String itemId;

    @Column(nullable=false, length=64)
    private int points;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
