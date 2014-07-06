package com.gaoshin.stock.model;

import com.gaoshin.sorma.annotation.Table;

@Table (
        keyColumn="id",
        autoId=true,
        create={
                "create table StockGroup (" +
                        "id INTEGER primary key autoincrement " +
                        ", name text " +
                        ", defaultItem Integer " +
                ")"
        }
)
public class StockGroup {
    private Integer id;
    private String name;
    private Integer defaultItem;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDefaultItem() {
        return defaultItem;
    }

    public void setDefaultItem(Integer defaultItem) {
        this.defaultItem = defaultItem;
    }
}
