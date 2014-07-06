package com.gaoshin.groupon.api;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Division {
    private String id;
    private String name;
    private String country;
    private String timezone;
    private Integer timezoneOffsetInSeconds;
    private String timezoneIdentifier;
    private Float lat;
    private Float lng;
    private Boolean isNowMerchantEnabled;
    private Boolean isNowCustomerEnabled;
    private Boolean isRewardEnabled;
    private AreaList areas;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public AreaList getAreas() {
        return areas;
    }

    public void setAreas(AreaList areas) {
        this.areas = areas;
    }

}
