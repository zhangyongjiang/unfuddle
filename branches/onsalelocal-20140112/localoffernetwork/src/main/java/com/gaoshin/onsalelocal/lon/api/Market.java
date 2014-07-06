package com.gaoshin.onsalelocal.lon.api;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Market {
	private String key;
	private String city;
	private String state;
	private String country;
	private String timezone;
	private Float lat;
	private Float lng;
	private Boolean national;
	private String status;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public Boolean getNational() {
		return national;
	}

	public void setNational(Boolean national) {
		this.national = national;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
