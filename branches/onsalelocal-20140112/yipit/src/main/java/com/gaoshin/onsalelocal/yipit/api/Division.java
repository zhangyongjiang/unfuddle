package com.gaoshin.onsalelocal.yipit.api;

public class Division {
	private String name;
	private String slug;
	private String active;
	private String time_zone_diff;
	private Float lat;
	private Float lon;
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
