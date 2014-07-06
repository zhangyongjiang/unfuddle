package com.gaoshin.appbooster.entity;

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
    @Column(nullable=false, length=64) private String name;
    @Column(nullable=false) private int birthYear;
    @Column(nullable=false) private int gender;
    @Column(nullable=true, length=64) private String login;
    @Column(nullable=true, length=64) private String password;
    @Column(nullable=true, length=64) private String mobile;
    @Column(nullable=true, length=64) private String validationCode;
    @Column(nullable=false, length=64) @Enumerated(EnumType.STRING) private UserStatus status;
    @Column(nullable=false, length=64) @Enumerated(EnumType.STRING) private UserType type;
    @Column(nullable=true, length=1023) private String url;
    @Column(nullable=true, length=1023) private String icon;
    @Column(nullable=true, length=1023) private String description;
    
    public User() {
        status = UserStatus.Blocked;
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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(String validationCode) {
        this.validationCode = validationCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }
}
