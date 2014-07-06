package com.gaoshin.onsalelocal.groupon.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import common.db.entity.DbEntity;

@Entity
@Table
public class Division extends DbEntity {
	@Column(nullable=true, length=127) private String name;
	@Column(nullable=true, length=127) private String country;
	@Column(nullable=true, length=127) private String timezone;
	@Column(nullable=true) private Integer timezoneOffsetInSeconds;
	@Column(nullable=true, length=127) private String timezoneIdentifier;
	@Column(nullable=true, length=127) private Float lat;
	@Column(nullable=true, length=127) private Float lng;
	@Column(nullable=true, length=127) private Boolean isNowMerchantEnabled;
	@Column(nullable=true, length=127) private Boolean isNowCustomerEnabled;
	@Column(nullable=true, length=127) private Boolean isRewardEnabled;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Integer getTimezoneOffsetInSeconds() {
		return timezoneOffsetInSeconds;
	}

	public void setTimezoneOffsetInSeconds(Integer timezoneOffsetInSeconds) {
		this.timezoneOffsetInSeconds = timezoneOffsetInSeconds;
	}

	public String getTimezoneIdentifier() {
		return timezoneIdentifier;
	}

	public void setTimezoneIdentifier(String timezoneIdentifier) {
		this.timezoneIdentifier = timezoneIdentifier;
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

	public Boolean getIsNowMerchantEnabled() {
		return isNowMerchantEnabled;
	}

	public void setIsNowMerchantEnabled(Boolean isNowMerchantEnabled) {
		this.isNowMerchantEnabled = isNowMerchantEnabled;
	}

	public Boolean getIsNowCustomerEnabled() {
		return isNowCustomerEnabled;
	}

	public void setIsNowCustomerEnabled(Boolean isNowCustomerEnabled) {
		this.isNowCustomerEnabled = isNowCustomerEnabled;
	}

	public Boolean getIsRewardEnabled() {
		return isRewardEnabled;
	}

	public void setIsRewardEnabled(Boolean isRewardEnabled) {
		this.isRewardEnabled = isRewardEnabled;
	}
	
}
