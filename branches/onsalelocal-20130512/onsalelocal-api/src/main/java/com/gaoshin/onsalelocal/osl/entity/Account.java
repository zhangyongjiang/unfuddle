package com.gaoshin.onsalelocal.osl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import common.db.entity.DbEntity;

@Entity
@Table
@XmlRootElement
public class Account extends DbEntity {
	@Column(nullable = true, length = 64)
	private String userId;

	@Column(nullable = true, length = 64)
	@Enumerated(EnumType.STRING)
	private AccountType type;
	
	@Column(nullable = true, length = 64)
	private String extId;
	
	@Column(nullable = true, length = 1024)
	private String token;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	public String getExtId() {
		return extId;
	}

	public void setExtId(String extId) {
		this.extId = extId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
