package com.gaoshin.points.server.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
public class TransactionEntity implements Serializable {
    @Id @GeneratedValue(generator="Shard0IdGenerator")
    @Column(length=64) 
    @GenericGenerator(name="Shard0IdGenerator", strategy="com.gaoshin.points.server.shard.Shard0IdGenerator")
	private String id;
    
    @Column(nullable=false, length=64)
    private String buyerUserId;

    @Column(nullable=false, length=64)
    private String sellerUserId;

    @Column(nullable=false, length=64)
    private String itemId;

    @Column(nullable=false)
    private int points;

    @Column(nullable=false)
    private long time;

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

	public String getBuyerUserId() {
		return buyerUserId;
	}

	public void setBuyerUserId(String buyerUserId) {
		this.buyerUserId = buyerUserId;
	}

	public String getSellerUserId() {
		return sellerUserId;
	}

	public void setSellerUserId(String sellerUserId) {
		this.sellerUserId = sellerUserId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
