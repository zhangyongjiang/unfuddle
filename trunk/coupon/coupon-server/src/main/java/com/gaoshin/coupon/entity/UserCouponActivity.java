package com.gaoshin.coupon.entity;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

//@Entity
//@Table
//@XmlRootElement
public class UserCouponActivity extends DbEntity {
    @Column(nullable=false, length=64) private String userId;
    @Column(nullable=false, length=64) private String couponId;
    @Column(nullable=false, length=64) @Enumerated(EnumType.STRING) private ActivityType type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }
}
