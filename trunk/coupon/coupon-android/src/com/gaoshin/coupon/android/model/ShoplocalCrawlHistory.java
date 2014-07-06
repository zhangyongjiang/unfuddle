package com.gaoshin.coupon.android.model;

import com.gaoshin.sorma.annotation.Table;

@Table(
        name = "ShoplocalCrawlHistory",
        keyColumn = "id",
        autoId = true,
        create = {
                "create table ShoplocalCrawlHistory (" +
                        " id text primary key " +
                        ", created bigint " +
                        ", updated bigint " +
                        ", city varchar(255) " +
                        ", state varhchar(32) " +
                        ", category varchar(64) " +
                        ", status varchar(16) " +
                        ", total integer " +
                        ", url varchar(1024) " +
                        ", fetched integer " +
                        ", page integer " +
                        ")"
        })
public class ShoplocalCrawlHistory {
    private String id;
    private long created;
    private long updated;
    private String city;
    private String state;
    private String category;
    private String url;
    private FetchStatus status;
    private int total;
    private int fetched;
    private int page;

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

    public int getFetched() {
        return fetched;
    }

    public void setFetched(int fetched) {
        this.fetched = fetched;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
