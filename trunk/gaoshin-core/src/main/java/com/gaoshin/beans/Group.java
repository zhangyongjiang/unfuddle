package com.gaoshin.beans;

import java.util.Calendar;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Group {
    private Long id;
    private User owner;
    private String name;
    private String description;
    private Calendar createTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
