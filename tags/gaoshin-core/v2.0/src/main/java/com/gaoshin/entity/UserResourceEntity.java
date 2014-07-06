package com.gaoshin.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gaoshin.beans.Post;
import com.gaoshin.beans.UserFileType;

@Entity
@Table(name = "user_res")
public class UserResourceEntity extends GenericEntity {
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;
    
    @Column(nullable = false, length = 64)
    private String mimetype;

    @Column(nullable = false, length = 64)
    @Enumerated(EnumType.STRING)
    private UserFileType type;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(length = 128)
    private String path;

    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createTime;

    public UserResourceEntity() {
    }

    public UserResourceEntity(Post bean) {
        super(bean);
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setType(UserFileType type) {
        this.type = type;
    }

    public UserFileType getType() {
        return type;
    }

}
