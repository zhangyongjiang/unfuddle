package com.gaoshin.onsalelocal.api;

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
public class Deal extends DbEntity {
	@Column(nullable=false) private long start;
	@Column(nullable=false) private long end;
	@Column(nullable=false, length=255) private String title; 
	@Column(nullable=true, length=500000) @Lob private String description; 
	@Column(nullable=false, length=64) private String source; 
	@Column(nullable=false, length=64) private String sourceId; 
	@Column(nullable=true, length=64) private String rootSource; 
	@Column(nullable=true, length=1023) private String smallImg; 
	@Column(nullable=true, length=1023) private String largeImg; 
	@Column(nullable=true, length=127) private String highlights; 
	@Column(nullable=true, length=255) private String price; 
	@Column(nullable=true, length=255) private String discount; 
	@Column(nullable=true, length=1023) private String url; 
	@Column(nullable=true, length=64) private String merchantId; 
	@Column(nullable=true, length=32) private String phone; 
	@Column(nullable=true, length=255) private String tags; 
	@Column(nullable=true, length=32) @Enumerated(EnumType.STRING) private OfferStatus status; 
	@Column(nullable = false) private int popularity;
	@Column(nullable = false) private int likes;
	@Column(nullable = false) private int views;
	@Column(nullable = false) private int comments;
	@Column(nullable = true) private Integer width;
	@Column(nullable = true) private Integer height;
	
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSmallImg() {
		return smallImg;
	}

	public void setSmallImg(String smallImg) {
		this.smallImg = smallImg;
	}

	public String getLargeImg() {
		return largeImg;
	}

	public void setLargeImg(String largeImg) {
		this.largeImg = largeImg;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getHighlights() {
		return highlights;
	}

	public void setHighlights(String highlights) {
		this.highlights = highlights;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getRootSource() {
		return rootSource;
	}

	public void setRootSource(String rootSource) {
		this.rootSource = rootSource;
	}

	public int getPopularity() {
	    return popularity;
    }

	public void setPopularity(int popularity) {
	    this.popularity = popularity;
    }

	public int getLikes() {
	    return likes;
    }

	public void setLikes(int likes) {
	    this.likes = likes;
    }

	public int getViews() {
	    return views;
    }

	public void setViews(int views) {
	    this.views = views;
    }

	public int getComments() {
	    return comments;
    }

	public void setComments(int comments) {
	    this.comments = comments;
    }

	public Integer getWidth() {
	    return width;
    }

	public void setWidth(Integer width) {
	    this.width = width;
    }

	public Integer getHeight() {
	    return height;
    }

	public void setHeight(Integer height) {
	    this.height = height;
    }

	public OfferStatus getStatus() {
	    return status;
    }

	public void setStatus(OfferStatus status) {
	    this.status = status;
    }

}
