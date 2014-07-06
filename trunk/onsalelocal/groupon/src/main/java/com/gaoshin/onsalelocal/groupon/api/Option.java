package com.gaoshin.onsalelocal.groupon.api;

public class Option {
    private String id;
    private String customFields;
    private String soldQuantityMessage;
    private Integer discountPercent;
    private Price discount;
    private Boolean bookable;
    private Integer minimumPurchaseQuantity;
    private Boolean isLimitedQuantity;
    private Price price;
    private Price value;
    private String title;
    private Boolean isSoldOut;
    private Integer maximumPurchaseQuantity;
    private String buyUrl;
    private Integer expiresInDays;
    private DetailList details;
    private String expiresAt;
    private Integer soldQuantity;
    private LocationList redemptionLocations;
    private String externalUrl;
    private Integer remainingQuantity;
    private Integer initialQuantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomFields() {
        return customFields;
    }

    public void setCustomFields(String customFields) {
        this.customFields = customFields;
    }

    public String getSoldQuantityMessage() {
        return soldQuantityMessage;
    }

    public void setSoldQuantityMessage(String soldQuantityMessage) {
        this.soldQuantityMessage = soldQuantityMessage;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Price getDiscount() {
        return discount;
    }

    public void setDiscount(Price discount) {
        this.discount = discount;
    }

    public Boolean getBookable() {
        return bookable;
    }

    public void setBookable(Boolean bookable) {
        this.bookable = bookable;
    }

    public Integer getMinimumPurchaseQuantity() {
        return minimumPurchaseQuantity;
    }

    public void setMinimumPurchaseQuantity(Integer minimumPurchaseQuantity) {
        this.minimumPurchaseQuantity = minimumPurchaseQuantity;
    }

    public Boolean getIsLimitedQuantity() {
        return isLimitedQuantity;
    }

    public void setIsLimitedQuantity(Boolean isLimitedQuantity) {
        this.isLimitedQuantity = isLimitedQuantity;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Price getValue() {
        return value;
    }

    public void setValue(Price value) {
        this.value = value;
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

    public Integer getMaximumPurchaseQuantity() {
        return maximumPurchaseQuantity;
    }

    public void setMaximumPurchaseQuantity(Integer maximumPurchaseQuantity) {
        this.maximumPurchaseQuantity = maximumPurchaseQuantity;
    }

    public String getBuyUrl() {
        return buyUrl;
    }

    public void setBuyUrl(String buyUrl) {
        this.buyUrl = buyUrl;
    }

    public Integer getExpiresInDays() {
        return expiresInDays;
    }

    public void setExpiresInDays(Integer expiresInDays) {
        this.expiresInDays = expiresInDays;
    }

    public DetailList getDetails() {
        return details;
    }

    public void setDetails(DetailList details) {
        this.details = details;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Integer getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public Integer getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(Integer remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public Integer getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(Integer initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public LocationList getRedemptionLocations() {
        return redemptionLocations;
    }

    public void setRedemptionLocations(LocationList redemptionLocations) {
        this.redemptionLocations = redemptionLocations;
    }
}
