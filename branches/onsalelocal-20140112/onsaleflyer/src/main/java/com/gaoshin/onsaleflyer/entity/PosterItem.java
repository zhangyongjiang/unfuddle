package com.gaoshin.onsaleflyer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import common.db.entity.DbEntity;

@Entity
@Table
@XmlRootElement
public class PosterItem extends DbEntity {
    @Column(nullable=false, length=64) private String posterId;
    @Column(nullable=false) private int x;
    @Column(nullable=false) private int y;
    @Column(nullable=false, length=64) @Enumerated(EnumType.STRING) private Shape shape;
	@Column(nullable=true, length=10000) @Lob private String param; 

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getPosterId() {
		return posterId;
	}

	public void setPosterId(String container) {
		this.posterId = container;
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
