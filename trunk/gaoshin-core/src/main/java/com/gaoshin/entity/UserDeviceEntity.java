package com.gaoshin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.gaoshin.business.PhoneInfo;

@Entity
@Table(name = "user_device")
public class UserDeviceEntity extends GenericEntity {
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column
    private int callState;

    @Column
    private int dataActivity;

    @Column
    private int dataState;

    @Column(length = 127)
    private String deviceId;

    @Column(length = 127)
    private String deviceSoftwareVersion;

    @Column(length = 127)
    private String line1Number;

    @Column(length = 127)
    private String networkCountryIso;

    @Column(length = 127)
    private String networkOperator;

    @Column(length = 127)
    private String networkOperatorName;

    @Column
    private int networkType;

    @Column
    private int phoneType;

    @Column(length = 127)
    private String simCountryIso;

    @Column(length = 127)
    private String simOperator;

    @Column(length = 127)
    private String simOperatorName;

    @Column(length = 127)
    private String simSerialNumber;

    @Column
    private int simState;

    @Column(length = 127)
    private String subscriberId;

    @Column(length = 127)
    private String voiceMailAlphaTag;

    @Column(length = 127)
    private String voiceMailNumber;

    @Column
    private int deviceWidth;

    @Column
    private int deviceHeight;

    @Column(length = 127)
    private String app;

    @Column(length = 127)
    private Integer version;

    @Column(length = 127)
    private String c2dmid;

    public UserDeviceEntity() {
    }

    public UserDeviceEntity(PhoneInfo bean) {
        super(bean);
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public int getCallState() {
        return callState;
    }

    public void setCallState(int callState) {
        this.callState = callState;
    }

    public int getDataActivity() {
        return dataActivity;
    }

    public void setDataActivity(int dataActivity) {
        this.dataActivity = dataActivity;
    }

    public int getDataState() {
        return dataState;
    }

    public void setDataState(int dataState) {
        this.dataState = dataState;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceSoftwareVersion() {
        return deviceSoftwareVersion;
    }

    public void setDeviceSoftwareVersion(String deviceSoftwareVersion) {
        this.deviceSoftwareVersion = deviceSoftwareVersion;
    }

    public String getLine1Number() {
        return line1Number;
    }

    public void setLine1Number(String line1Number) {
        this.line1Number = line1Number;
    }

    public String getNetworkCountryIso() {
        return networkCountryIso;
    }

    public void setNetworkCountryIso(String networkCountryIso) {
        this.networkCountryIso = networkCountryIso;
    }

    public String getNetworkOperator() {
        return networkOperator;
    }

    public void setNetworkOperator(String networkOperator) {
        this.networkOperator = networkOperator;
    }

    public String getNetworkOperatorName() {
        return networkOperatorName;
    }

    public void setNetworkOperatorName(String networkOperatorName) {
        this.networkOperatorName = networkOperatorName;
    }

    public int getNetworkType() {
        return networkType;
    }

    public void setNetworkType(int networkType) {
        this.networkType = networkType;
    }

    public int getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(int phoneType) {
        this.phoneType = phoneType;
    }

    public String getSimCountryIso() {
        return simCountryIso;
    }

    public void setSimCountryIso(String simCountryIso) {
        this.simCountryIso = simCountryIso;
    }

    public String getSimOperator() {
        return simOperator;
    }

    public void setSimOperator(String simOperator) {
        this.simOperator = simOperator;
    }

    public String getSimOperatorName() {
        return simOperatorName;
    }

    public void setSimOperatorName(String simOperatorName) {
        this.simOperatorName = simOperatorName;
    }

    public String getSimSerialNumber() {
        return simSerialNumber;
    }

    public void setSimSerialNumber(String simSerialNumber) {
        this.simSerialNumber = simSerialNumber;
    }

    public int getSimState() {
        return simState;
    }

    public void setSimState(int simState) {
        this.simState = simState;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getVoiceMailAlphaTag() {
        return voiceMailAlphaTag;
    }

    public void setVoiceMailAlphaTag(String voiceMailAlphaTag) {
        this.voiceMailAlphaTag = voiceMailAlphaTag;
    }

    public String getVoiceMailNumber() {
        return voiceMailNumber;
    }

    public void setVoiceMailNumber(String voiceMailNumber) {
        this.voiceMailNumber = voiceMailNumber;
    }

    public int getDeviceWidth() {
        return deviceWidth;
    }

    public void setDeviceWidth(int deviceWidth) {
        this.deviceWidth = deviceWidth;
    }

    public int getDeviceHeight() {
        return deviceHeight;
    }

    public void setDeviceHeight(int deviceHeight) {
        this.deviceHeight = deviceHeight;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getC2dmid() {
        return c2dmid;
    }

    public void setC2dmid(String c2dmid) {
        this.c2dmid = c2dmid;
    }

}
