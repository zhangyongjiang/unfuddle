package com.gaoshin.appbooster.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table
@XmlRootElement
public class Reward extends DbEntity {
    @Column(nullable=false, length=64) private String campaignId;
    @Column(nullable=false, length=128) private String name;
    @Column(nullable=false, length=50000) @Lob private String description;
    @Column(nullable=false ) private int points;
    @Column(nullable=false, length=64) @Enumerated(EnumType.STRING) private RewardStatus status;
    @Column(nullable=false, length=64) @Enumerated(EnumType.STRING) private RewardType type;
    @Column(nullable=false, length=64) private String subtype;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public RewardStatus getStatus() {
        return status;
    }

    public void setStatus(RewardStatus status) {
        this.status = status;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RewardType getType() {
        return type;
    }

    public void setType(RewardType type) {
        this.type = type;
    }
}
