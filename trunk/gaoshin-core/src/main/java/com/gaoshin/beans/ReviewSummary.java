package com.gaoshin.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReviewSummary {
    private Long id;
    private Long targetId;
    private String targetType;
    private int thumbsup;
    private int thumbsdown;
    private Calendar lastUpdateTime;
    private List<Review> reviews = new ArrayList<Review>();

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setThumbsup(int thumbsup) {
        this.thumbsup = thumbsup;
    }

    public int getThumbsup() {
        return thumbsup;
    }

    public void setThumbsdown(int thumbsdown) {
        this.thumbsdown = thumbsdown;
    }

    public int getThumbsdown() {
        return thumbsdown;
    }

    public void setReviews(List<Review> reviewEntities) {
        this.reviews = reviewEntities;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setLastUpdateTime(Calendar lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Calendar getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
