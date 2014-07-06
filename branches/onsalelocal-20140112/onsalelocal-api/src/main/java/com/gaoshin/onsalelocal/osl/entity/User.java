package com.gaoshin.onsalelocal.osl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import common.db.entity.DbEntity;

@Entity
@Table
@XmlRootElement
public class User extends DbEntity {
    @Column(nullable=true, length=64) private String firstName;
    @Column(nullable=true, length=64) private String lastName;
    @Column(nullable=true) private int birthYear;
    @Column(nullable=true, length=64) @Enumerated(EnumType.STRING) private Gender gender;
    @Column(nullable=true, length=64) private String login;
    @Column(nullable=true, length=64) private String password;
    @Column(nullable=true, length=64) private String mobile;
    @Column(nullable=true, length=64) private String zipcode;
    @Column(nullable=true, length=64) private String tempPassword;
    @Column(nullable=true, length=64) private String deviceId;
    @Column(nullable=true, length=1023) private String img;
    @Column(nullable=false, length=64) @Enumerated(EnumType.STRING) private AccountStatus status;
    @Column(nullable=false, length=64) @Enumerated(EnumType.STRING) private NotificationStatus noti;
    
    public User() {
        status = AccountStatus.Inactive;
        noti = NotificationStatus.Enable;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getZipcode() {
	    return zipcode;
    }

	public void setZipcode(String zipcode) {
	    this.zipcode = zipcode;
    }

	public String getDeviceId() {
	    return deviceId;
    }

	public void setDeviceId(String deviceId) {
	    this.deviceId = deviceId;
    }

	public String getImg() {
	    return img;
    }

	public void setImg(String img) {
	    this.img = img;
    }

	public String getTempPassword() {
	    return tempPassword;
    }

	public void setTempPassword(String tempPassword) {
	    this.tempPassword = tempPassword;
    }

	public NotificationStatus getNoti() {
	    return noti;
    }

	public void setNoti(NotificationStatus noti) {
	    this.noti = noti;
    }
}
