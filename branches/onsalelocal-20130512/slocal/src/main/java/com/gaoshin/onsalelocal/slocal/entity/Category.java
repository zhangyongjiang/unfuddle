package com.gaoshin.onsalelocal.slocal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import common.db.entity.DbEntity;

@Entity
@Table
@XmlRootElement
public class Category extends DbEntity {
    @Column(nullable=false, length=64) private String name;
    @Column(nullable=true, length=64) private String source;
    @Column(nullable=true, length=1023) private String url;
    @Column(nullable=true, length=1023) private String url2;
    @Column(nullable=true, length=64) private String parentId;
    @Column(nullable=true, length=255) private String topDealsUrl;

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

	public String getUrl2() {
		return url2;
	}

	public void setUrl2(String url2) {
		this.url2 = url2;
	}

	public String getTopDealsUrl() {
	    return topDealsUrl;
    }

	public void setTopDealsUrl(String topDealsUrl) {
	    this.topDealsUrl = topDealsUrl;
    }
}
