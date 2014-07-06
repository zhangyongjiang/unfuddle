package com.gaoshin.points.server.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
public class ItemEntity implements Serializable {
    @Id @GeneratedValue(generator="SameUserShardIdGenerator")
    @Column(length=64) 
    @GenericGenerator(name="SameUserShardIdGenerator", strategy="com.gaoshin.points.server.shard.SameUserShardIdGenerator")
	private String id;
    
    @Column(length=255)
    private String name;

    @Column(nullable=false, length=64)
    private String userId;

    @Column(nullable=false)
    private long startTime;

    @Column(nullable=false)
    private long expire;

    @Column(nullable=false)
    private int points;

    @Lob
    @Column
    private String description;
    
	@Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private ItemStatus status;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public ItemStatus getStatus() {
		return status;
	}

	public void setStatus(ItemStatus status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
