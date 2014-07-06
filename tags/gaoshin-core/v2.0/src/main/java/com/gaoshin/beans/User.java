package com.gaoshin.beans;

import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotEmpty;

import com.gaoshin.business.PhoneInfo;
import common.util.DesEncrypter;

@XmlRootElement
public class User {
    private Long id;

    @NotEmpty
    @Length(max = 32, min = 1, profiles = { "signup" })
    private String name;

    private boolean enabled;

    @NotEmpty
    @Length(max = 32, min = 1, profiles = { "signup, login" })
    private String phone;

    @Length(max = 32, min = 1, profiles = { "signup, login" })
    private String password;

    private Location currentLocation;
    private String icon;
    private String interests;
    private String introduction;
    private String clientType;
    private Gender gender;
    private Integer birthYear;
    private DimensionValueList profiles;
    private PhoneInfo deviceInfo;
    private UserRole role;
    private Calendar regtime;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getEncryptedId() {
        return DesEncrypter.selfencrypt(phone, name, id + "");
    }

    public static User decryptId(String encrypted) {
        List<String> data = DesEncrypter.selfdecrypt(encrypted);
        User user = new User();
        user.setName(data.get(1));
        user.setId(Long.parseLong(data.get(2)));
        user.setPhone(data.get(0));
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setProfiles(DimensionValueList profiles) {
        this.profiles = profiles;
    }

    public DimensionValueList getProfiles() {
        return profiles;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
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

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }

    public void setDeviceInfo(PhoneInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public PhoneInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserRole getRole() {
        if (role == null)
            return id == null ? UserRole.GUEST : UserRole.USER;
        else
            return role;
    }

    public boolean isSameUser(User user) {
        if (id != null) {
            if (id.equals(user.getId()))
                return true;
        }
        if (phone != null) {
            if (phone.equals(user.getPhone()))
                return true;
        }
        if (name != null) {
            if (name.equals(user.getName()))
                return true;
        }
        return false;
    }

    public boolean isSuperUser() {
        return UserRole.ADMIN.equals(role) || UserRole.SUPER.equals(role);
    }

    public int getAge() {
        return Calendar.getInstance().get(Calendar.YEAR) - birthYear;
    }

    public void setRegtime(Calendar regtime) {
        this.regtime = regtime;
    }

    public Calendar getRegtime() {
        return regtime;
    }
}
