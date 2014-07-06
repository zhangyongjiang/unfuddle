package com.gaoshin.coupon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table
@XmlRootElement
public class User extends DbEntity {
    @Column(nullable=true, length=64) private String firstName;
    @Column(nullable=true, length=64) private String lastName;
    @Column(nullable=false) private int birthYear;
    @Column(nullable=false) private int gender;
    @Column(nullable=true, length=64) private String login;
    @Column(nullable=true, length=64) private String password;
    @Column(nullable=true, length=64) private String mobile;
    @Column(nullable=true, length=64) private String mobilePassword;
    @Column(nullable=false, length=64) @Enumerated(EnumType.STRING) private AccountStatus status;
    
    public User() {
        status = AccountStatus.Inactive;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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

    public String getMobilePassword() {
        return mobilePassword;
    }

    public void setMobilePassword(String mobilePassword) {
        this.mobilePassword = mobilePassword;
    }
}
