package com.gaoshin.points.server.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
public class CashierEntity implements Serializable {
    @Id @GeneratedValue(generator="CashierIdGenerator")
    @GenericGenerator(name="CashierIdGenerator", strategy="com.gaoshin.points.server.shard.CashierIdGenerator")
    @Column(length=64)
    private String id;
    
    @Column(nullable=false, length=255)
    private String name;

    @Column(nullable=false, length=64)
    private String password;

    @Column(nullable=false, length=64)
    private String merchantId;

    @Column(nullable=false, length=64)
    private String cashierMerchantId;

	@Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

	public String getId() {
		return id;
	}

    public void setId(String id) {
        this.id = id;
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getCashierMerchantId() {
		return cashierMerchantId;
	}

	public void setCashierMerchantId(String cashierId) {
		this.cashierMerchantId = cashierId;
	}

}
