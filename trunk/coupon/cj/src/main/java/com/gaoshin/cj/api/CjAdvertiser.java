package com.gaoshin.cj.api;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "advertiser") 
public class CjAdvertiser {
    private String advertiserId;
    private String accountStatus;
    private String sevenDayEpc;
    private String threeMonthEpc;
    private String language;
    private String advertiserName;
    private String programUrl;
    private String relationshipStatus;
    private String mobileSupported;
    private String mobileTrackingCertified;
    private String networkRank;
    private List<String> primaryCategory;
    private String performanceIncentives;
    private String actions;
    private List<String> linkTypes;

    @XmlElement(name = "account-status")
    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    @XmlElement(name = "seven-day-epc")
    public String getSevenDayEpc() {
        return sevenDayEpc;
    }

    public void setSevenDayEpc(String sevenDayEpc) {
        this.sevenDayEpc = sevenDayEpc;
    }

    @XmlElement(name = "three-month-epc")
    public String getThreeMonthEpc() {
        return threeMonthEpc;
    }

    public void setThreeMonthEpc(String threeMonthEpc) {
        this.threeMonthEpc = threeMonthEpc;
    }

    @XmlElement(name = "language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @XmlElement(name = "advertiser-name")
    public String getAdvertiserName() {
        return advertiserName;
    }

    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = advertiserName;
    }

    @XmlElement(name = "program-url")
    public String getProgramUrl() {
        return programUrl;
    }

    public void setProgramUrl(String programUrl) {
        this.programUrl = programUrl;
    }

    @XmlElement(name = "relationship-status")
    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    @XmlElement(name = "mobile-supported")
    public String getMobileSupported() {
        return mobileSupported;
    }

    public void setMobileSupported(String mobileSupported) {
        this.mobileSupported = mobileSupported;
    }

    @XmlElement(name = "mobile-tracking-certified")
    public String getMobileTrackingCertified() {
        return mobileTrackingCertified;
    }

    public void setMobileTrackingCertified(String mobileTrackingCertified) {
        this.mobileTrackingCertified = mobileTrackingCertified;
    }

    @XmlElement(name = "network-rank")
    public String getNetworkRank() {
        return networkRank;
    }

    public void setNetworkRank(String networkRank) {
        this.networkRank = networkRank;
    }

    @XmlElement(name = "primary-category")
    public List<String> getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(List<String> primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    @XmlElement(name = "performance-incentives")
    public String getPerformanceIncentives() {
        return performanceIncentives;
    }

    public void setPerformanceIncentives(String performanceIncentives) {
        this.performanceIncentives = performanceIncentives;
    }

    @XmlElement(name = "actions")
    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    @XmlElement(name = "link-types")
    public List<String> getLinkTypes() {
        return linkTypes;
    }

    public void setLinkTypes(List<String> linkTypes) {
        this.linkTypes = linkTypes;
    }

    @XmlElement(name = "advertiser-id")
    public String getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(String advertiserId) {
        this.advertiserId = advertiserId;
    }

}
