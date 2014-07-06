package com.gaoshin.coupon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table
@XmlRootElement
public class ShoplocalCrawlHistory extends DbEntity {
    @Column(nullable=true, length=64) private String updateId;
    @Column(nullable=true, length=64) private String city;
    @Column(nullable=true, length=64) private String state;
    @Column(nullable=true, length=64) private String category;
    @Column(nullable=true, length=1023) private String url;
    @Column(nullable=true, length=64) @Enumerated(EnumType.STRING) private FetchStatus status;
    @Column(nullable=true) private int page;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FetchStatus getStatus() {
        return status;
    }

    public void setStatus(FetchStatus status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
}
