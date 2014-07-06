package com.gaoshin.amazon;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlRootElement;

import com.gaoshin.beans.Location;
import com.gaoshin.beans.User;

@XmlRootElement
public class NodeInterest {
    private Long id;
    private Calendar lastUpdate;
    private Calendar expire;
    private boolean sell;
    private String url;
    private String info;
    private BigDecimal price;
    private User user;
    private Location location;
    private AwsBrowseNode node;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setNode(AwsBrowseNode node) {
        this.node = node;
    }

    public AwsBrowseNode getNode() {
        return node;
    }

}
