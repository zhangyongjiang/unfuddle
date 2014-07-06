package com.gaoshin.onsalelocal.groupon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import common.db.entity.DbEntity;

@Entity
@Table
public class Advertiser extends DbEntity {
    @Column(length=128) private String accountStatus;
    @Column(length=128) private String sevenDayEpc;
    @Column(length=128) private String threeMonthEpc;
    @Column(length=128) private String language;
    @Column(length=128) private String advertiserName;
    @Column(length=128) private String programUrl;
    @Column(length=128) private String relationshipStatus;
    @Column(length=128) private String mobileSupported;
    @Column(length=128) private String mobileTrackingCertified;
    @Column(length=128) private String networkRank;
    @Column(length=128) private String performanceIncentives;
    @Column(length=128) private String status;
    @Column(length=32) private String joined;

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getSevenDayEpc() {
        return sevenDayEpc;
    }

    public void setSevenDayEpc(String sevenDayEpc) {
        this.sevenDayEpc = sevenDayEpc;
    }

    public String getThreeMonthEpc() {
        return threeMonthEpc;
    }

    public void setThreeMonthEpc(String threeMonthEpc) {
        this.threeMonthEpc = threeMonthEpc;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAdvertiserName() {
        return advertiserName;
    }

    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = advertiserName;
    }

    public String getProgramUrl() {
        return programUrl;
    }

    public void setProgramUrl(String programUrl) {
        this.programUrl = programUrl;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public String getMobileSupported() {
        return mobileSupported;
    }

    public void setMobileSupported(String mobileSupported) {
        this.mobileSupported = mobileSupported;
    }

    public String getMobileTrackingCertified() {
        return mobileTrackingCertified;
    }

    public void setMobileTrackingCertified(String mobileTrackingCertified) {
        this.mobileTrackingCertified = mobileTrackingCertified;
    }

    public String getNetworkRank() {
        return networkRank;
    }

    public void setNetworkRank(String networkRank) {
        this.networkRank = networkRank;
    }

    public String getPerformanceIncentives() {
        return performanceIncentives;
    }

    public void setPerformanceIncentives(String performanceIncentives) {
        this.performanceIncentives = performanceIncentives;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

}
