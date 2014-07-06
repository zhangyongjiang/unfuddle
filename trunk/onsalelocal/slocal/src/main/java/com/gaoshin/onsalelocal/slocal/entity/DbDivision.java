package com.gaoshin.onsalelocal.slocal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import common.db.entity.DbEntity;

@Entity
@Table
public class DbDivision extends DbEntity {
	@Column(nullable = true, length = 64)
	private String name;
	
	@Column(nullable = true, length = 64)
	private String slug;
	
	@Column(nullable = true, length = 32)
	private String active;
	
	@Column(nullable = true, length = 64)
	private String time_zone_diff;

	@Column(nullable = true)
	private Float lat;
	
	@Column(nullable = true)
	private Float lon;
	
	@Column(nullable = true, length = 1023)
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

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getTime_zone_diff() {
		return time_zone_diff;
	}

	public void setTime_zone_diff(String time_zone_diff) {
		this.time_zone_diff = time_zone_diff;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Float getLon() {
		return lon;
	}

	public void setLon(Float lon) {
		this.lon = lon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
