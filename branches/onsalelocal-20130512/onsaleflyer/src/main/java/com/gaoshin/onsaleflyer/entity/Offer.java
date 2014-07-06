package com.gaoshin.onsaleflyer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import common.db.entity.DbEntity;

@Entity
@Table
@XmlRootElement
public class Offer extends DbEntity {
	@Column(nullable = false, length = 64)
	private String title;
	
	@Column(nullable = true, length = 1023)
	private String highlights;
	
	@Column(nullable = true, length = 10000)
	@Lob
	private String description;
	
	@Column(nullable = true)
	private Integer price;
	
	@Column(nullable = false)
	private long start;
	
	@Column(nullable = false)
	private long end;
	
	@Column(nullable = false, length = 64)
	private String posterId;

	@Column(nullable = false, length = 64) private String userId;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public String getPosterId() {
		return posterId;
	}

	public void setPosterId(String posterId) {
		this.posterId = posterId;
	}

	public String getHighlights() {
		return highlights;
	}

	public void setHighlights(String highlights) {
		this.highlights = highlights;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
