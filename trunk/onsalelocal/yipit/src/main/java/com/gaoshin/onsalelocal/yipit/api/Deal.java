package com.gaoshin.onsalelocal.yipit.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Deal {
	private String id;
	private String date_added;
	private String end_date;
	private String active;
	private Value discount;
	private Value price;
	private Value value;
	private String title;
	private String yipit_title;
	private String url;
	private String yipit_url;
	private String mobile_url;
	private Images images;
	private Division division;
	private List<Tag> tags = new ArrayList<Tag>();
	private Business business;
	private Source source;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate_added() {
		return date_added;
	}

	public void setDate_added(String date_added) {
		this.date_added = date_added;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public Value getDiscount() {
		return discount;
	}

	public void setDiscount(Value discount) {
		this.discount = discount;
	}

	public Value getPrice() {
		return price;
	}

	public void setPrice(Value price) {
		this.price = price;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYipit_title() {
		return yipit_title;
	}

	public void setYipit_title(String yipit_title) {
		this.yipit_title = yipit_title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getYipit_url() {
		return yipit_url;
	}

	public void setYipit_url(String yipit_url) {
		this.yipit_url = yipit_url;
	}

	public String getMobile_url() {
		return mobile_url;
	}

	public void setMobile_url(String mobile_url) {
		this.mobile_url = mobile_url;
	}

	public Images getImages() {
		return images;
	}

	public void setImages(Images images) {
		this.images = images;
	}

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public long getStartTime() {
		if(date_added == null)
			return 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.parse(date_added).getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public long getEndTime() {
		if(end_date == null)
			return 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.parse(end_date).getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
