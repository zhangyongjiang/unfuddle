package com.gaoshin.top;

import com.gaoshin.sorma.annotation.Column;
import com.gaoshin.sorma.annotation.Ddl;
import com.gaoshin.sorma.annotation.Table;

@Table(
        name = "job",
        keyColumn = "_id",
        autoId = true,
        create = {
        "create table if not exists job (" +
                " _id integer PRIMARY KEY autoincrement" +
                ", shortcut integer" +
                ", cron varchar(64)" +
                ")"
        },
        ddls = {
                @Ddl(
                        yyyyMMddHHmmss = "20110525000000",
                        value = "create table if not exists job (" +
                                " _id integer PRIMARY KEY autoincrement" +
                                ", shortcut integer" +
                                ", cron varchar(64)" +
                                ")"
                )
        })
public class Job {
    @Column(name = "_id")
    private Integer id;

    @Column
    private Integer shortcut;

    @Column
    private String cron;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setShortcut(Integer shortcut) {
        this.shortcut = shortcut;
    }

    public Integer getShortcut() {
        return shortcut;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getCron() {
        return cron;
    }

}
