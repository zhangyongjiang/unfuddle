package com.gaoshin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.gaoshin.beans.MsgType;

@Entity
@Table(name = "notification")
public class NotificationEntity extends GenericEntity {
    @JoinColumn(name = "FROM_ID")
    private UserEntity from;

    @JoinColumn(name = "TO_ID")
    private UserEntity to;

    @Column
    private Long sentTime;

    @Column
    private Long receiveTime;

    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private MsgType type;

    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private MsgType subtype;

    @Column(length = 1023)
    private String msg;

    @Column(length = 255)
    private String title;

    @Column(length = 1023)
    private String url;

    @Column(name = "is_read")
    private boolean read = false;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setSentTime(Long sentTime) {
        this.sentTime = sentTime;
    }

    public Long getSentTime() {
        return sentTime;
    }

    public void setReceiveTime(Long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Long getReceiveTime() {
        return receiveTime;
    }

    public void setType(MsgType type) {
        this.type = type;
    }

    public MsgType getType() {
        return type;
    }

    public void setSubtype(MsgType subtype) {
        this.subtype = subtype;
    }

    public MsgType getSubtype() {
        return subtype;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isRead() {
        return read;
    }

    public void setFrom(UserEntity from) {
        this.from = from;
    }

    public UserEntity getFrom() {
        return from;
    }

    public void setTo(UserEntity to) {
        this.to = to;
    }

    public UserEntity getTo() {
        return to;
    }
}
