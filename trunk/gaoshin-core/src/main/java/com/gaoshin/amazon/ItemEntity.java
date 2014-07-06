package com.gaoshin.amazon;

import java.util.Calendar;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import common.util.reflection.ReflectionUtil;

@Entity
@Table(name = "aws_item")
public class ItemEntity {
    @Id
    @Column(length = 31)
    private String asin;

    @Column(length = 255)
    private String title;

    @Column
    private boolean loaded = false;

    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar lastUpdate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @Column(name = "star")
    private Float star;

    @Column(name = "reviews")
    private Integer totalReviews;

    @Column
    private Integer star5;

    @Column
    private Integer star4;

    @Column
    private Integer star3;

    @Column
    private Integer star2;

    @Column
    private Integer star1;

    public Float getStar() {
        return star;
    }

    public void setStar(Float star) {
        this.star = star;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
    }

    public Integer getStar5() {
        return star5;
    }

    public void setStar5(Integer star5) {
        this.star5 = star5;
    }

    public Integer getStar4() {
        return star4;
    }

    public void setStar4(Integer star4) {
        this.star4 = star4;
    }

    public Integer getStar3() {
        return star3;
    }

    public void setStar3(Integer star3) {
        this.star3 = star3;
    }

    public Integer getStar2() {
        return star2;
    }

    public void setStar2(Integer star2) {
        this.star2 = star2;
    }

    public Integer getStar1() {
        return star1;
    }

    public void setStar1(Integer star1) {
        this.star1 = star1;
    }

    public ItemEntity() {
    }

    public ItemEntity(Object bean) {
        copyFrom(bean);
    }

    public void copyFrom(Object bean) {
        ReflectionUtil.copyPrimeProperties(this, bean);
    }

    public AwsItem getBean() {
        try {
            AwsItem bean = new AwsItem();
            ReflectionUtil.copyPrimeProperties(bean, this);
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getAsin() {
        return asin;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setLastUpdate(Calendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Calendar getLastUpdate() {
        return lastUpdate;
    }
}
