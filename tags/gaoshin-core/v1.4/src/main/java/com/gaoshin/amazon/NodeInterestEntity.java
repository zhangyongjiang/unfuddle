package com.gaoshin.amazon;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gaoshin.entity.GenericEntity;
import com.gaoshin.entity.UserEntity;
import com.gaoshin.entity.UserLocationEntity;

@Entity
@Table(name = "aws_node_intest")
public class NodeInterestEntity extends GenericEntity {
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar lastUpdate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar expire;

    @Column
    private boolean sell;

    @Column(length = 1023)
    private String url;

    @Column(length = 255)
    private String info;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @JoinColumn(name = "location_id")
    private UserLocationEntity userLocationEntity;

    @JoinColumn(name = "node_id", nullable = false)
    @ManyToOne
    private BrowseNodeEntity nodeEntity;

    public NodeInterestEntity() {
    }

    public NodeInterestEntity(NodeInterest bean) {
        super(bean);
    }

    public Calendar getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Calendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Calendar getExpire() {
        return expire;
    }

    public void setExpire(Calendar expire) {
        this.expire = expire;
    }

    public boolean isSell() {
        return sell;
    }

    public void setSell(boolean sell) {
        this.sell = sell;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserLocationEntity getUserLocationEntity() {
        return userLocationEntity;
    }

    public void setUserLocationEntity(UserLocationEntity userLocationEntity) {
        this.userLocationEntity = userLocationEntity;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setNodeEntity(BrowseNodeEntity nodeEntity) {
        this.nodeEntity = nodeEntity;
    }

    public BrowseNodeEntity getNodeEntity() {
        return nodeEntity;
    }
}
