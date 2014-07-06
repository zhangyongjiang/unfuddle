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
public class Application extends DbEntity {
    @Column(nullable=false, length=64) private String name;
    @Column(nullable=false, length=64)  @Enumerated(EnumType.STRING) private ApplicationType type;
    @Column(nullable=false, length=64) private String marketId;
    @Column(nullable=false, length=64) private String userId;
    @Column(nullable=true, length=1023) private String icon;
    @Column(nullable=true, length=1023) private String image;
    @Column(nullable=true, length=50000) @Lob private String description;
    @Column(nullable=false, length=64) @Enumerated(EnumType.STRING) private ApplicationStatus status;

    public ApplicationType getType() {
        return type;
    }

    public void setType(ApplicationType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

}
