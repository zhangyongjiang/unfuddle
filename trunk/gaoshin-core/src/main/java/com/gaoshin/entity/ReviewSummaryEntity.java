package com.gaoshin.entity;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gaoshin.beans.ReviewTarget;

@Entity
@Table(name = "review_sum")
public class ReviewSummaryEntity extends GenericEntity {
    
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "target_type", nullable = false, length = 64)
    @Enumerated(EnumType.STRING)
    private ReviewTarget targetType;

    @Column
    private int thumbsup;

    @Column
    private int thumbsdown;
    
    @Column(name = "last_update", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar lastUpdateTime;

    @OneToMany(mappedBy = "reviewSummaryEntity")
    private List<ReviewEntity> reviewEntities;

    public ReviewSummaryEntity() {
    }

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

    public void setReviewEntities(List<ReviewEntity> reviewEntities) {
        this.reviewEntities = reviewEntities;
    }

    public List<ReviewEntity> getReviewEntities() {
        return reviewEntities;
    }

    public void setLastUpdateTime(Calendar lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Calendar getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setTargetType(ReviewTarget targetType) {
        this.targetType = targetType;
    }

    public ReviewTarget getTargetType() {
        return targetType;
    }

}
