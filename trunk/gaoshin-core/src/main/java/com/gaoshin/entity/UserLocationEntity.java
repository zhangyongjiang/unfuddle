package com.gaoshin.entity;

import java.util.Calendar;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gaoshin.beans.Location;

@Entity
@Table(name = "user_location")
public class UserLocationEntity extends GenericEntity {
    @Column
    private Float latitude;

    @Column
    private Float longitude;

    @Column(length = 256)
    private String address;

    @Column(length = 128)
    private String city;

    @Column(length = 128)
    private String state;

    @Column(length = 20)
    private String zipcode;

    @Column(length = 255)
    private String country;

    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar lastUpdate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column
    private boolean device;

    public UserLocationEntity() {
    }

    public UserLocationEntity(Location bean) {
        super(bean);
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

    public void setLastUpdate(Calendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void resetLastUpdate() {
        this.lastUpdate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    }

    public Calendar getLastUpdate() {
        return lastUpdate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setDevice(boolean device) {
        this.device = device;
    }

    public boolean isDevice() {
        return device;
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
