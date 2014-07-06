package com.gaoshin.beans;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.sf.oval.constraint.NotEmpty;

import common.web.XCalendarAdapter;

@XmlRootElement
public class Location {
	private Long id;

    @NotEmpty
    private Float latitude;

    @NotEmpty
    private Float longitude;
    private Calendar firstUpdate;
    private Calendar lastUpdate;

    @NotEmpty
    private String address;

    @NotEmpty
    private String city;

    @NotEmpty
    private String state;

    private String zipcode;

    @NotEmpty
    private String country;

    private boolean device;
    private User user;
    private boolean current;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public Float getLatitude() {
		return latitude;
	}

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
		return longitude;
	}

    public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    @XmlJavaTypeAdapter(XCalendarAdapter.class)
    public void setLastUpdate(Calendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Calendar getLastUpdate() {
        return lastUpdate;
    }

    @XmlJavaTypeAdapter(XCalendarAdapter.class)
    public void setFirstUpdate(Calendar firstUpdate) {
        this.firstUpdate = firstUpdate;
    }

    public Calendar getFirstUpdate() {
        return firstUpdate;
    }

    public void setDevice(boolean device) {
        this.device = device;
    }

    public boolean isDevice() {
        return device;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }
}
