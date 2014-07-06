package com.gaoshin.beans;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Review {
    private Long id;
    private User author;
    private ReviewSummary reviewSummaryEntity;
    private Long targetId;
    private ReviewTarget targetType;
    private String title;
    private String content;
    private int thumbsup;
    private int thumbsdown;
    private Calendar createTime;

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

    public void setReviewSummaryEntity(ReviewSummary reviewSummaryEntity) {
        this.reviewSummaryEntity = reviewSummaryEntity;
    }

    public ReviewSummary getReviewSummaryEntity() {
        return reviewSummaryEntity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }

    public void setTargetType(ReviewTarget targetType) {
        this.targetType = targetType;
    }

    public ReviewTarget getTargetType() {
        return targetType;
    }

}
