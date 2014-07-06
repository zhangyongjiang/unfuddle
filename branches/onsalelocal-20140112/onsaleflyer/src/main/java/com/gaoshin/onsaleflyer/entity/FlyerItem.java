package com.gaoshin.onsaleflyer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import common.db.entity.DbEntity;

@Entity
@Table
@XmlRootElement
public class FlyerItem extends DbEntity {
	@Column(nullable = false, length = 64)
	private String flyerId;

	@Column(nullable = false, length = 64)
	private String offerId;

	@Column(nullable = false)
	private int x;

	@Column(nullable = false)
	private int y;

	public String getFlyerId() {
		return flyerId;
	}

	public void setFlyerId(String flyerId) {
		this.flyerId = flyerId;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
