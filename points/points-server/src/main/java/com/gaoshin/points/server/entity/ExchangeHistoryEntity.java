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
public class ExchangeHistoryEntity implements Serializable {
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

    @Column(nullable=false)
    private int earn;

    @Column(nullable=false)
    private long time;

    @Column(nullable=false, length=64)
    private String transactionId;

    @Column(length=64)
    private String merchantId;

    @Column(length=64)
    private String cashierId;

    @Column(length=255)
    private String merchantName;

    @Column(length=1023)
    private String note;

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

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String otherUserId) {
		this.merchantId = otherUserId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String otherUserName) {
		this.merchantName = otherUserName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCashierId() {
		return cashierId;
	}

	public void setCashierId(String cashierId) {
		this.cashierId = cashierId;
	}

	public int getEarn() {
		return earn;
	}

	public void setEarn(int earn) {
		this.earn = earn;
	}

}
