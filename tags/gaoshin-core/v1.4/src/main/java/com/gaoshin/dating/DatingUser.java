package com.gaoshin.dating;

import javax.xml.bind.annotation.XmlRootElement;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotEmpty;

import com.gaoshin.beans.Gender;
import com.gaoshin.beans.Location;

@XmlRootElement
public class DatingUser {
    @NotEmpty
    @Length(max = 32, min = 1)
    private String name;

    @NotEmpty
    @Length(max = 20, min = 6)
    private String phone;

    @NotEmpty
    @Length(min = 1)
    private String password;

    @NotEmpty
    private Gender gender;

    @NotEmpty
    private Integer birthyear;

    @NotEmpty
    private Location location;

    @NotEmpty
    private String interests;

    @NotEmpty
    private DatingPurpose lookingfor;

    @NotEmpty
    private String clientType;

    private String deviceInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(Integer birthyear) {
        this.birthyear = birthyear;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }

    public void setLookingfor(DatingPurpose lookingfor) {
        this.lookingfor = lookingfor;
    }

    public DatingPurpose getLookingfor() {
        return lookingfor;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }
}