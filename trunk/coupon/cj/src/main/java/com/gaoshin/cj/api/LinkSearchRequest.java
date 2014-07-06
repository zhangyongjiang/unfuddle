package com.gaoshin.cj.api;

import java.util.ArrayList;
import java.util.List;

public class LinkSearchRequest extends CjRequest {
    private List<String> advertiserIds = new ArrayList<String>();
    private String keywords;
    private String category;
    private String linkType;
    private String promotionType;
    private Long promotionStartDate;
    private Long promotionEndDate;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendUrl(sb , "advertiser-ids", advertiserIds);
        appendUrl(sb, "keywords", keywords);
        appendUrl(sb, "category", category);
        appendUrl(sb, "link-type", linkType);
        appendUrl(sb, "promotion-type", promotionType);
        appendUrl(sb, "promotion-start-date", promotionStartDate, "MM/dd/yyyy");
        appendUrl(sb, "promotion-end-date", promotionEndDate, "MM/dd/yyyy");
        return super.toString() + sb.toString();
    }
    
    public List<String> getAdvertiserIds() {
        return advertiserIds;
    }

    public void setAdvertiserIds(List<String> advertiserIds) {
        this.advertiserIds = advertiserIds;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public Long getPromotionStartDate() {
        return promotionStartDate;
    }

    public void setPromotionStartDate(Long promotionStartDate) {
        this.promotionStartDate = promotionStartDate;
    }

    public Long getPromotionEndDate() {
        return promotionEndDate;
    }

    public void setPromotionEndDate(Long promotionEndDate) {
        this.promotionEndDate = promotionEndDate;
    }

}
