package com.gaoshin.onsalelocal.osl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import common.db.entity.DbEntity;

@Entity
@Table
@XmlRootElement
public class StoreComment extends DbEntity {
	@Column(nullable = true, length = 64)
	private String storeId;
	@Column(nullable = true, length = 64)
	private String userId;
	@Column(nullable = true, length = 64)
	private String content;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStoreId() {
	    return storeId;
    }

	public void setStoreId(String storeId) {
	    this.storeId = storeId;
    }
}
