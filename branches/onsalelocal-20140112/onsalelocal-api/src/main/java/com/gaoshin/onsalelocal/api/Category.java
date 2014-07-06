package com.gaoshin.onsalelocal.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table
@XmlRootElement
public class Category {
	@Id @Column(nullable=false, length=64) private String id;
	@Column(length=64) private String parentId;
	@Column(nullable=false, length=64)private String name;
	@Column(nullable=false)private int offerCount;
	@Column(nullable=false, length=1023)private String image;
	
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getOfferCount() {
	    return offerCount;
    }

	public void setOfferCount(int offerCount) {
	    this.offerCount = offerCount;
    }

	public String getImage() {
	    return image;
    }

	public void setImage(String image) {
	    this.image = image;
    }
}
