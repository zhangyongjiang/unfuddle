package com.gaoshin.entity;

import java.util.Calendar;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gaoshin.beans.Group;

@Entity
@Table(name = "groups")
public class GroupEntity extends GenericEntity {
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @Column(nullable = false, length = 127, unique = true)
    private String name;
    
    @Lob
    private String description;
    
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    
    public GroupEntity() {
    }

    public GroupEntity(Group bean) {
        super(bean);
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
