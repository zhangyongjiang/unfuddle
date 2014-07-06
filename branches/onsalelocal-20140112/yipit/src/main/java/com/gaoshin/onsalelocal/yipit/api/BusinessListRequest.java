package com.gaoshin.onsalelocal.yipit.api;


public class BusinessListRequest extends YipitRequest {
	private Float lat;
	private Float lon;
	private Integer radius;
	private String phone;
	private String division;
	
	public BusinessListRequest() {
		setUri("http://api.yipit.com/v1/businesses");
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

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}
}
