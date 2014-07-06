package com.gaoshin.stock.model;

import com.gaoshin.sorma.annotation.Table;

@Table(
        keyColumn = "id",
        autoId = true,
        create = {
                "create table GroupItem (" +
                        "id INTEGER primary key autoincrement " +
                        ", groupId INTEGER " +
                        ", sym TEXT " +
                ")"
        }
)
public class GroupItem {
    private Integer id;
    private Integer groupId;
    private String sym;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getSym() {
        return sym;
    }

    public void setSym(String sym) {
        this.sym = sym;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
