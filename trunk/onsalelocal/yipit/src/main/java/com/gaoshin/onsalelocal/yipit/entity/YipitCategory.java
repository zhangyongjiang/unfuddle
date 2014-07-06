package com.gaoshin.onsalelocal.yipit.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import common.db.entity.DbEntity;

@Entity
@Table
public class YipitCategory extends DbEntity {
	private String name;
	private String slug;
	private String url;

	public String getName() {
	    return name;
    }

	public void setName(String name) {
	    this.name = name;
    }

	public String getSlug() {
	    return slug;
    }

	public void setSlug(String slug) {
	    this.slug = slug;
    }

	public String getUrl() {
	    return url;
    }

	public void setUrl(String url) {
	    this.url = url;
    }
}
