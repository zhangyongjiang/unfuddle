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
