package com.gaoshin.entity;

import java.util.Calendar;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gaoshin.beans.Gender;
import com.gaoshin.beans.User;
import com.gaoshin.beans.UserRole;
import common.util.MD5;

@Entity
@Table(name = "USERS")
public class UserEntity extends GenericEntity {
    @Column(name = "USERNAME", nullable = false, length = 50, unique = true)
    private String name;

    @Column(length = 50)
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false, length = 32, unique = true)
    private String phone;

    @Column(name = "regtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar regtime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @JoinColumn(name = "current_loc")
    @MapKey
    private UserLocationEntity currentLocation;

    @Column(length = 32)
    private String clientType;

    @Column(length = 255)
    private String icon;

    @Column(length = 255)
    private String interests;

    @Column(length = 1023)
    private String introduction;

    @Column(length = 16)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "byear")
    private Integer birthYear;

    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public UserEntity() {
    }

    public UserEntity(User user) {
        super(user);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isRightPassword(String password) {
        return password.equals(this.password) || this.password == null || this.password.equals(MD5.md5(password));
    }

    public void setRegtime(Calendar regtime) {
        this.regtime = regtime;
    }

    public Calendar getRegtime() {
        return regtime;
    }

    public void setCurrentLocation(UserLocationEntity currentLocation) {
        this.currentLocation = currentLocation;
    }

    public UserLocationEntity getCurrentLocation() {
        return currentLocation;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientType() {
        return clientType;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getInterests() {
        return interests;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isAdminUser() {
        return UserRole.ADMIN.equals(role) || UserRole.SUPER.equals(role);
    }

    public boolean isSuperUser() {
        return UserRole.SUPER.equals(role);
    }

    public boolean isBadGuy() {
        return UserRole.BADGUY.equals(role);
    }
}
