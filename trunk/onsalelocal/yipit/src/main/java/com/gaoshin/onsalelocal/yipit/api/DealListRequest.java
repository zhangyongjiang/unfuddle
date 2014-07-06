package com.gaoshin.onsalelocal.yipit.api;


public class DealListRequest extends YipitRequest {
	private Float lat;
	private Float lon;
	private Integer radius;
	private String division;
	private String source;
	private String phone;
	private String tag;
	private String paid;
	private Integer limit;

	public DealListRequest() {
		setUri("http://api.yipit.com/v1/deals");
		setLimit(5000);
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

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public DealResponse call() throws Exception {
		return super.call(DealResponse.class);
	}
}
