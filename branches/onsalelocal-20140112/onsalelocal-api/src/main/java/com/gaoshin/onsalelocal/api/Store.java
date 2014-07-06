package com.gaoshin.onsalelocal.api;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import common.db.entity.DbEntity;

@Entity
@Table
@XmlRootElement
public class Store extends DbEntity {
    @Column(nullable=true, length=255) private String name;
    @Column(nullable=true, length=255) private String address;
    @Column(nullable=true, length=128) private String address2;
    @Column(nullable=true, length=128) private String city;
    @Column(nullable=true, length=128) private String state;
    @Column(nullable=true, length=128) private String country;
    @Column(nullable=true, length=16) private String zipcode;
    @Column(nullable=true, length=32) private String phone;
    @Column(nullable=true, length=32) private String email;
    @Column(nullable=true, length=1023) private String web;
    @Column(nullable=true, length=1023) private String logo;
    @Column(precision=10, scale=5) private BigDecimal latitude;
    @Column(precision=10, scale=5) private BigDecimal longitude;
    @Column(nullable=true, length=64) private String companyId;
    @Column(nullable=true, length=64) private String chainStoreId;
	@Column(nullable=false, length=64) private String ownerUserId; 
	@Column(length=64) private String category; 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChainStoreId() {
        return chainStoreId;
    }

    public void setChainStoreId(String chainStoreId) {
        this.chainStoreId = chainStoreId;
    }

	public String getOwnerUserId() {
	    return ownerUserId;
    }

	public void setOwnerUserId(String ownerUserId) {
	    this.ownerUserId = ownerUserId;
    }

	public String getCategory() {
	    return category;
    }

	public void setCategory(String category) {
	    this.category = category;
    }

}
