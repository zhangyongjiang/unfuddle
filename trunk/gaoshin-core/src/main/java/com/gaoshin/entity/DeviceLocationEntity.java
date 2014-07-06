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
@Table(name = "device_loc")
public class DeviceLocationEntity extends GenericEntity {
    @Column(columnDefinition = "FLOAT", length = 12, precision = 12, scale = 5)
    private Float latitude;

    @Column(columnDefinition = "FLOAT", length = 12, precision = 12, scale = 5)
    private Float longitude;

    @Column(length = 255)
    private String address;

    @Column(length = 128)
    private String city;

    @Column(length = 128)
    private String state;

    @Column(length = 20)
    private String zipcode;

    @Column(length = 255)
    private String country;

    @Column(name = "FIRST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar firstUpdate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar lastUpdate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public DeviceLocationEntity() {
    }

    public DeviceLocationEntity(Location bean) {
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

    public void setFirstUpdate(Calendar firstUpdate) {
        this.firstUpdate = firstUpdate;
    }

    public Calendar getFirstUpdate() {
        return firstUpdate;
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
