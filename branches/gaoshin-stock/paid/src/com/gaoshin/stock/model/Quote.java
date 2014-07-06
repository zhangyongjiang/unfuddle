package com.gaoshin.stock.model;

import com.gaoshin.sorma.annotation.Table;

@Table (
        keyColumn="id",
        autoId=true,
        create={
                "create table quote (" +
                        "id INTEGER primary key autoincrement " +
                        ", sym text " +
                        ", lastUpdateTime bigint " +
                        ", price float " +
                        ", created bigint " +
                ")"
        }
)
public class Quote {
    private Integer id;
    private String sym;
    private String lastUpdateTime;
    private float price;
    private Long created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSym() {
        return sym;
    }

    public void setSym(String sym) {
        this.sym = sym;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String time) {
        this.lastUpdateTime = time;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

}
