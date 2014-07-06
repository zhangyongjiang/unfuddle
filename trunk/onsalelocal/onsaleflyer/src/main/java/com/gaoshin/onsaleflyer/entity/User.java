package com.gaoshin.onsaleflyer.entity;

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
    @Column(nullable=false, length=64) private String name;
    @Column(nullable=false, length=64) private String login;
    @Column(nullable=false, length=64) private String password;
    @Column(nullable=true, length=64) private String phone;
    @Column(nullable=true, length=255) private String adddress;
    @Column(nullable=true, length=255) private String adddress2;
    @Column(nullable=true, length=255) private String city;
    @Column(nullable=true, length=255) private String state;
    @Column(nullable=true, length=255) private String country;
    @Column(nullable=true, length=1023) private String logo;
    @Column(nullable=true, length=1023) private String url;
    @Column(nullable=true, length=255) private String email;
    @Column(nullable=false, length=64) @Enumerated(EnumType.STRING) private UserStatus status;
    @Column(nullable=true, length=255) private String fbtoken;
    
    public User() {
        status = UserStatus.Inactive;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFbtoken() {
		return fbtoken;
	}

	public void setFbtoken(String fbtoken) {
		this.fbtoken = fbtoken;
	}

	public String getAdddress() {
		return adddress;
	}

	public void setAdddress(String adddress) {
		this.adddress = adddress;
	}

	public String getAdddress2() {
		return adddress2;
	}

	public void setAdddress2(String adddress2) {
		this.adddress2 = adddress2;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
