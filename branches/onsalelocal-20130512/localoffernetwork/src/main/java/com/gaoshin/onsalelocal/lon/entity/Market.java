package com.gaoshin.onsalelocal.lon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import common.db.entity.DbEntity;

@Entity
@Table
public class Market extends DbEntity {
	@Column(nullable = true, length = 127)
	private String city;
	@Column(nullable = true, length = 64)
	private String state;
	@Column(nullable = true, length = 32)
	private String country;
	@Column(nullable = true, length = 64)
	private String timezone;
	@Column(nullable = true)
	private Float lat;
	@Column(nullable = true)
	private Float lng;
	@Column(nullable = true)
	private String national;
	@Column(nullable = true, length = 32)
	private String status;

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

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Float getLng() {
		return lng;
	}

	public void setLng(Float lng) {
		this.lng = lng;
	}

	public String getNational() {
		return national;
	}

	public void setNational(String national) {
		this.national = national;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
