package com.gaoshin.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gaoshin.beans.Review;
import com.gaoshin.beans.ReviewTarget;

@Entity
@Table(name = "reviews")
public class ReviewEntity extends GenericEntity {
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;
    
    @ManyToOne
    @JoinColumn(name = "sum_id")
    private ReviewSummaryEntity reviewSummaryEntity;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "target_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewTarget targetType;

    @Column(length = 255)
    private String title;

    @Column(length = 1023)
    private String content;

    @Column
    private int thumbsup;

    @Column
    private int thumbsdown;
    
    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createTime;

    public ReviewEntity() {
    }

    public ReviewEntity(Review bean) {
        super(bean);
    }

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
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

    public void setReviewSummaryEntity(ReviewSummaryEntity reviewSummaryEntity) {
        this.reviewSummaryEntity = reviewSummaryEntity;
    }

    public ReviewSummaryEntity getReviewSummaryEntity() {
        return reviewSummaryEntity;
    }

    public void setTargetType(ReviewTarget targetType) {
        this.targetType = targetType;
    }

    public ReviewTarget getTargetType() {
        return targetType;
    }

}
