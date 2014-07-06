package com.gaoshin.cj.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "link") 
public class CjLink {
    private String advertiserId;
    private String clickCommission;
    private String creativeHeight;
    private String creativeWidth;
    private String leadCommission;
    private String linkCodeHtml;
    private String linkCodeJavascript;
    private String linkDestination;
    private String linkDescription;
    private String linkId;
    private String linkName;
    private String linkType;
    private String advertiserName;
    private String promotionType;
    private String promotionStartDate;
    private String promotionEndDate;
    private String relationshipStatus;
    private String salecommission;
    private String sevenDayEpc;
    private String threeMonthEpc;

    @XmlElement(name = "advertiser-id")
    public String getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(String advertiserId) {
        this.advertiserId = advertiserId;
    }

    @XmlElement(name = "click-commission")
    public String getClickCommission() {
        return clickCommission;
    }

    public void setClickCommission(String clickCommission) {
        this.clickCommission = clickCommission;
    }

    @XmlElement(name = "creative-height")
    public String getCreativeHeight() {
        return creativeHeight;
    }

    public void setCreativeHeight(String creativeHeight) {
        this.creativeHeight = creativeHeight;
    }

    @XmlElement(
            name = "creative-width")
    public String getCreativeWidth() {
        return creativeWidth;
    }

    public void setCreativeWidth(String creativeWidth) {
        this.creativeWidth = creativeWidth;
    }

    @XmlElement(
            name = "lead-commission")
    public String getLeadCommission() {
        return leadCommission;
    }

    public void setLeadCommission(String leadCommission) {
        this.leadCommission = leadCommission;
    }

    @XmlElement(
            name = "link-code-html")
    public String getLinkCodeHtml() {
        return linkCodeHtml;
    }

    public void setLinkCodeHtml(String linkCodeHtml) {
        this.linkCodeHtml = linkCodeHtml;
    }

    @XmlElement(
            name = "link-code-javascript")
    public String getLinkCodeJavascript() {
        return linkCodeJavascript;
    }

    public void setLinkCodeJavascript(String linkCodeJavascript) {
        this.linkCodeJavascript = linkCodeJavascript;
    }

    @XmlElement(
            name = "link-destination")
    public String getLinkDestination() {
        return linkDestination;
    }

    public void setLinkDestination(String linkDestination) {
        this.linkDestination = linkDestination;
    }

    @XmlElement(
            name = "link-description")
    public String getLinkDescription() {
        return linkDescription;
    }

    public void setLinkDescription(String linkDescription) {
        this.linkDescription = linkDescription;
    }

    @XmlElement(
            name = "link-id")
    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    @XmlElement(
            name = "link-name")
    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    @XmlElement(
            name = "link-type")
    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    @XmlElement(
            name = "advertiser-name")
    public String getAdvertiserName() {
        return advertiserName;
    }

    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = advertiserName;
    }

    @XmlElement(
            name = "promotion-type")
    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    @XmlElement(
            name = "promotion-start-date")
    public String getPromotionStartDate() {
        return promotionStartDate;
    }

    public void setPromotionStartDate(String promotionStartDate) {
        this.promotionStartDate = promotionStartDate;
    }

    @XmlElement(
            name = "promotion-end-date")
    public String getPromotionEndDate() {
        return promotionEndDate;
    }

    public void setPromotionEndDate(String promotionEndDate) {
        this.promotionEndDate = promotionEndDate;
    }

    @XmlElement(
            name = "relationship-status")
    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    @XmlElement(
            name = "sale-commission")
    public String getSalecommission() {
        return salecommission;
    }

    public void setSalecommission(String salecommission) {
        this.salecommission = salecommission;
    }

    @XmlElement(
            name = "seven-day-epc")
    public String getSevenDayEpc() {
        return sevenDayEpc;
    }

    public void setSevenDayEpc(String sevenDayEpc) {
        this.sevenDayEpc = sevenDayEpc;
    }

    @XmlElement(
            name = "three-month-epc")
    public String getThreeMonthEpc() {
        return threeMonthEpc;
    }

    public void setThreeMonthEpc(String threeMonthEpc) {
        this.threeMonthEpc = threeMonthEpc;
    }

}
