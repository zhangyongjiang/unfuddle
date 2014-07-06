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
public class Offer extends DbEntity {
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
	@Column(nullable=true, length=255) private String merchant; 
	@Column(nullable=true, length=1023) private String merchantLogo; 
	@Column(nullable=true, length=255) private String address; 
	@Column(nullable=true, length=255) private String city; 
	@Column(nullable=true, length=127) private String state; 
	@Column(nullable=true, length=32) private String country; 
	@Column(nullable=true, length=32) private String zipcode; 
	@Column(nullable=true, length=32) private String phone; 
	@Column(nullable=true, length=32) private Float latitude; 
	@Column(nullable=true, length=32) private Float longitude;
	@Column(nullable=true, length=64) private String category; 
	@Column(nullable=true, length=64) private String subcategory; 
	@Column(nullable=true, length=255) private String tags; 
	@Column(nullable=true, length=32) @Enumerated(EnumType.STRING) private String status; 
	@Column(nullable=true) private Integer popularity; 

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		if(subcategory != null && subcategory.trim().length() ==0)
			subcategory = null;
		this.subcategory = subcategory;
	}

	public String getMerchantLogo() {
		return merchantLogo;
	}

	public void setMerchantLogo(String merchantLogo) {
		this.merchantLogo = merchantLogo;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRootSource() {
		return rootSource;
	}

	public void setRootSource(String rootSource) {
		this.rootSource = rootSource;
	}

	public Integer getPopularity() {
	    return popularity;
    }

	public void setPopularity(Integer popularity) {
	    this.popularity = popularity;
    }

}
