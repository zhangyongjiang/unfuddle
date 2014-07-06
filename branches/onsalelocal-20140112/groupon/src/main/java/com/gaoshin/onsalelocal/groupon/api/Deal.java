package com.gaoshin.onsalelocal.groupon.api;

public class Deal {
    private String id;
    private String grid4ImageUrl;
    private Merchant merchant;
    // grouponRating
    private String type;
    private Integer soldQuantityMessage;
    private String mediumImageUrl;
    private String locationNote;
    // limitedQuantityRemaining
    private String limitedQuantityRemaining;
    private String highlightsHtml;
    private String status;
    private String announcementTitle;
    private String dealUrl;
    private Boolean isNowDeal;
    // says
    private String grid6ImageUrl;
    // textAd
    private String shortAnnouncementTitle;
    private Boolean isOptionListComplete;
    private String title;
    private Boolean isSoldOut;
    // vip
    private Boolean shippingAddressRequired;
    private String endAt;
    private String sidebarImageUrl;
    private String pitchHtml;
    private Division division;
    private Boolean isTipped;
    private Integer soldQuantity;
    private String startAt;
    // channels
    private String placementPriority;
    private String largeImageUrl;
    private Boolean isAutoRefundEnabled;
    private String tippingPoint;
    private String finePrint;
    private String smallImageUrl;
    private TagList tags;
    private Boolean isTravelBookableDeal;
    private AreaList areas;
    private OptionList options;
    private Boolean isMerchandisingDeal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrid4ImageUrl() {
        return grid4ImageUrl;
    }

    public void setGrid4ImageUrl(String grid4ImageUrl) {
        this.grid4ImageUrl = grid4ImageUrl;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSoldQuantityMessage() {
        return soldQuantityMessage;
    }

    public void setSoldQuantityMessage(Integer soldQuantityMessage) {
        this.soldQuantityMessage = soldQuantityMessage;
    }

    public String getMediumImageUrl() {
        return mediumImageUrl;
    }

    public void setMediumImageUrl(String mediumImageUrl) {
        this.mediumImageUrl = mediumImageUrl;
    }

    public String getLocationNote() {
        return locationNote;
    }

    public void setLocationNote(String locationNote) {
        this.locationNote = locationNote;
    }

    public String getLimitedQuantityRemaining() {
        return limitedQuantityRemaining;
    }

    public void setLimitedQuantityRemaining(String limitedQuantityRemaining) {
        this.limitedQuantityRemaining = limitedQuantityRemaining;
    }

    public String getHighlightsHtml() {
        return highlightsHtml;
    }

    public void setHighlightsHtml(String highlightsHtml) {
        this.highlightsHtml = highlightsHtml;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public void setAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }

    public String getDealUrl() {
        return dealUrl;
    }

    public void setDealUrl(String dealUrl) {
        this.dealUrl = dealUrl;
    }

    public Boolean getIsNowDeal() {
        return isNowDeal;
    }

    public void setIsNowDeal(Boolean isNowDeal) {
        this.isNowDeal = isNowDeal;
    }

    public String getGrid6ImageUrl() {
        return grid6ImageUrl;
    }

    public void setGrid6ImageUrl(String grid6ImageUrl) {
        this.grid6ImageUrl = grid6ImageUrl;
    }

    public String getShortAnnouncementTitle() {
        return shortAnnouncementTitle;
    }

    public void setShortAnnouncementTitle(String shortAnnouncementTitle) {
        this.shortAnnouncementTitle = shortAnnouncementTitle;
    }

    public Boolean getIsOptionListComplete() {
        return isOptionListComplete;
    }

    public void setIsOptionListComplete(Boolean isOptionListComplete) {
        this.isOptionListComplete = isOptionListComplete;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIsSoldOut() {
        return isSoldOut;
    }

    public void setIsSoldOut(Boolean isSoldOut) {
        this.isSoldOut = isSoldOut;
    }

    public Boolean getShippingAddressRequired() {
        return shippingAddressRequired;
    }

    public void setShippingAddressRequired(Boolean shippingAddressRequired) {
        this.shippingAddressRequired = shippingAddressRequired;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getSidebarImageUrl() {
        return sidebarImageUrl;
    }

    public void setSidebarImageUrl(String sidebarImageUrl) {
        this.sidebarImageUrl = sidebarImageUrl;
    }

    public String getPitchHtml() {
        return pitchHtml;
    }

    public void setPitchHtml(String pitchHtml) {
        this.pitchHtml = pitchHtml;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public Boolean getIsTipped() {
        return isTipped;
    }

    public void setIsTipped(Boolean isTipped) {
        this.isTipped = isTipped;
    }

    public Integer getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getPlacementPriority() {
        return placementPriority;
    }

    public void setPlacementPriority(String placementPriority) {
        this.placementPriority = placementPriority;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public Boolean getIsAutoRefundEnabled() {
        return isAutoRefundEnabled;
    }

    public void setIsAutoRefundEnabled(Boolean isAutoRefundEnabled) {
        this.isAutoRefundEnabled = isAutoRefundEnabled;
    }

    public String getTippingPoint() {
        return tippingPoint;
    }

    public void setTippingPoint(String tippingPoint) {
        this.tippingPoint = tippingPoint;
    }

    public String getFinePrint() {
        return finePrint;
    }

    public void setFinePrint(String finePrint) {
        this.finePrint = finePrint;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public TagList getTags() {
        return tags;
    }

    public void setTags(TagList tags) {
        this.tags = tags;
    }

    public Boolean getIsTravelBookableDeal() {
        return isTravelBookableDeal;
    }

    public void setIsTravelBookableDeal(Boolean isTravelBookableDeal) {
        this.isTravelBookableDeal = isTravelBookableDeal;
    }

    public AreaList getAreas() {
        return areas;
    }

    public void setAreas(AreaList areas) {
        this.areas = areas;
    }

    public OptionList getOptions() {
        return options;
    }

    public void setOptions(OptionList options) {
        this.options = options;
    }

    public Boolean getIsMerchandisingDeal() {
        return isMerchandisingDeal;
    }

    public void setIsMerchandisingDeal(Boolean isMerchandisingDeal) {
        this.isMerchandisingDeal = isMerchandisingDeal;
    }
}
