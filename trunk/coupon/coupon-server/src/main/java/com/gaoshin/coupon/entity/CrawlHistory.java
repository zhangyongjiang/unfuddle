package com.gaoshin.coupon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table
@XmlRootElement
public class CrawlHistory extends DbEntity {
    @Column(nullable=true, length=64) private String parentStoreId;
    @Column(nullable=true, length=64) private String storeId;
    @Column(nullable=true, length=64) private String chainStoreId;
    @Column(nullable=true, length=64) private String zipcode;

    public String getParentStoreId() {
        return parentStoreId;
    }

    public void setParentStoreId(String parentStoreId) {
        this.parentStoreId = parentStoreId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getChainStoreId() {
        return chainStoreId;
    }

    public void setChainStoreId(String chainStoreId) {
        this.chainStoreId = chainStoreId;
    }

}
