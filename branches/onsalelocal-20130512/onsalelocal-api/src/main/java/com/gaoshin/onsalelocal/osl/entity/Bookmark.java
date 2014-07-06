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
public class Bookmark extends DbEntity {
	@Column(nullable = true, length = 64)
	private String userId;

	@Column(nullable = true, length = 64)
	private String offerId;

	@Column(nullable = true, length = 1023)
	private String param;

    @Column(nullable=false, length=64) @Enumerated(EnumType.STRING) private BookmarkType type;

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public BookmarkType getType() {
		return type;
	}

	public void setType(BookmarkType type) {
		this.type = type;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
}
